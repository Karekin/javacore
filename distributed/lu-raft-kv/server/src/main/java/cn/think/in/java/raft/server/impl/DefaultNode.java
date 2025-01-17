/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package cn.think.in.java.raft.server.impl;

import cn.think.in.java.raft.common.entity.*;
import cn.think.in.java.raft.server.Consensus;
import cn.think.in.java.raft.server.LogModule;
import cn.think.in.java.raft.server.Node;
import cn.think.in.java.raft.server.StateMachine;
import cn.think.in.java.raft.server.changes.ClusterMembershipChanges;
import cn.think.in.java.raft.server.changes.Result;
import cn.think.in.java.raft.common.RaftRemotingException;
import cn.think.in.java.raft.server.constant.StateMachineSaveType;
import cn.think.in.java.raft.server.current.RaftThreadPool;
import cn.think.in.java.raft.server.rpc.DefaultRpcServiceImpl;
import cn.think.in.java.raft.server.rpc.RpcService;
import cn.think.in.java.raft.server.util.LongConvert;
import cn.think.in.java.raft.common.rpc.DefaultRpcClient;
import cn.think.in.java.raft.common.rpc.Request;
import cn.think.in.java.raft.common.rpc.RpcClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 抽象机器节点, 初始为 follower, 角色随时变化.
 *
 * @author 莫那·鲁道
 */
@Getter
@Setter
@Slf4j
public class DefaultNode implements Node, ClusterMembershipChanges {

    /**
     * 选举时间间隔基数
     */
    public volatile long electionTime = 15 * 1000;
    /**
     * 上一次选举时间
     */
    public volatile long preElectionTime = 0;

    /**
     * 上次一心跳时间戳
     */
    public volatile long preHeartBeatTime = 0;
    /**
     * 心跳间隔基数
     */
    public final long heartBeatTick = 5 * 100;


    private HeartBeatTask heartBeatTask = new HeartBeatTask();
    private ElectionTask electionTask = new ElectionTask();
    private ReplicationFailQueueConsumer replicationFailQueueConsumer = new ReplicationFailQueueConsumer();

    private LinkedBlockingQueue<ReplicationFailModel> replicationFailQueue = new LinkedBlockingQueue<>(2048);


    /**
     * 节点当前状态
     *
     * @see NodeStatus
     */
    public volatile int status = NodeStatus.FOLLOWER;

    public PeerSet peerSet;

    volatile boolean running = false;

    /* ============ 所有服务器上持久存在的 ============= */

    /**
     * 服务器最后一次知道的任期号（初始化为 0，持续递增）
     */
    volatile long currentTerm = 0;
    /**
     * 在当前获得选票的候选人的 Id
     */
    volatile String votedFor;
    /**
     * 日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号
     */
    LogModule logModule;



    /* ============ 所有服务器上经常变的 ============= */

    /**
     * 已知的最大的已经被提交的日志条目的索引值
     */
    volatile long commitIndex;

    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增)
     */
    volatile long lastApplied = 0;

    /* ========== 在领导人里经常改变的(选举后重新初始化) ================== */

    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    Map<Peer, Long> nextIndexs;

    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    Map<Peer, Long> matchIndexs;

    /**
     * 什么时候 update 这两个追踪值？ledader 发送了”附加日志“请求，并获取到了返回值后
     */

    /* ============================== */

    public NodeConfig config;

    public RpcService rpcServer;

    public RpcClient rpcClient = new DefaultRpcClient();

    public StateMachine stateMachine;

    /* ============================== */

    /**
     * 一致性模块实现
     */
    Consensus consensus;

    ClusterMembershipChanges delegate;


    /* ============================== */

    private DefaultNode() {
    }

    public static DefaultNode getInstance() {
        return DefaultNodeLazyHolder.INSTANCE;
    }


    private static class DefaultNodeLazyHolder {

        private static final DefaultNode INSTANCE = new DefaultNode();
    }

    @Override
    public void init() throws Throwable {
        running = true;
        rpcServer.init();
        rpcClient.init();

        consensus = new DefaultConsensus(this);
        delegate = new ClusterMembershipChangesImpl(this);

        RaftThreadPool.scheduleWithFixedDelay(heartBeatTask, 500);
        RaftThreadPool.scheduleAtFixedRate(electionTask, 6000, 500);
        RaftThreadPool.execute(replicationFailQueueConsumer);

        LogEntry logEntry = logModule.getLast();
        if (logEntry != null) {
            currentTerm = logEntry.getTerm();
        }

        log.info("start success, selfId : {} ", peerSet.getSelf());
    }

    @Override
    public void setConfig(NodeConfig config) {
        this.config = config;
        stateMachine = StateMachineSaveType.getForType(config.getStateMachineSaveType()).getStateMachine();
        logModule = DefaultLogModule.getInstance();

        peerSet = PeerSet.getInstance();
        for (String s : config.getPeerAddrs()) {
            Peer peer = new Peer(s);
            peerSet.addPeer(peer);
            if (s.equals("localhost:" + config.getSelfPort())) {
                peerSet.setSelf(peer);
            }
        }

        rpcServer = new DefaultRpcServiceImpl(config.selfPort, this);
    }


    @Override
    public RvoteResult handlerRequestVote(RvoteParam param) {
        log.warn("handlerRequestVote will be invoke, param info : {}", param);
        return consensus.requestVote(param);
    }

    @Override
    public AentryResult handlerAppendEntries(AentryParam param) {
        if (param.getEntries() != null) {
            log.warn("node receive node {} append entry, entry content = {}", param.getLeaderId(), param.getEntries());
        }

        return consensus.appendEntries(param);
    }


    @Override
    public ClientKVAck redirect(ClientKVReq request) {
        Request r = Request.builder()
                .obj(request)
                .url(peerSet.getLeader().getAddr())
                .cmd(Request.CLIENT_REQ).build();

        return rpcClient.send(r);
    }

    /**
     * 客户端的每一个请求都包含一条被复制状态机执行的指令。
     * 领导人把这条指令作为一条新的日志条目附加到日志中去，然后并行的发起附加条目 RPCs 给其他的服务器，让他们复制这条日志条目。
     * 当这条日志条目被安全的复制（下面会介绍），领导人会应用这条日志条目到它的状态机中，然后把执行的结果返回给客户端。
     * 如果跟随者崩溃或者运行缓慢，再或者网络丢包，
     * 领导人会不断的重复尝试附加日志条目 RPCs （尽管已经回复了客户端）直到所有的跟随者都最终存储了所有的日志条目。
     *
     * @param request
     * @return
     */
    @Override
    public synchronized ClientKVAck handlerClientRequest(ClientKVReq request) {

        log.warn("handlerClientRequest handler {} operation,  and key : [{}], value : [{}]",
                ClientKVReq.Type.value(request.getType()), request.getKey(), request.getValue());

        if (status != NodeStatus.LEADER) {
            log.warn("I not am leader , only invoke redirect method, leader addr : {}, my addr : {}",
                    peerSet.getLeader(), peerSet.getSelf().getAddr());
            return redirect(request);
        }

        // TODO 读请求的话，follower 可以完成，没必要全都压给 leader
        if (request.getType() == ClientKVReq.GET) {
            LogEntry logEntry = stateMachine.get(request.getKey());
            if (logEntry != null) {
                return new ClientKVAck(logEntry);
            }
            return new ClientKVAck(null);
        }

        LogEntry logEntry = LogEntry.builder()
                .command(Command.builder().
                        key(request.getKey()).
                        value(request.getValue()).
                        build())
                .term(currentTerm)
                .build();

        // 预提交到本地日志, 给logEntry赋index值   TODO 预提交
        logModule.write(logEntry);
        log.info("write logModule success, logEntry info : {}, log index : {}", logEntry, logEntry.getIndex());

        final AtomicInteger success = new AtomicInteger(0);

        List<Future<Boolean>> futureList = new ArrayList<>();

        int count = 0;
        //  复制到其他机器
        for (Peer peer : peerSet.getPeersWithOutSelf()) {
            // TODO check self and RaftThreadPool
            count++;
            // 并行发起 RPC 复制.
            futureList.add(replication(peer, logEntry));
        }

        CountDownLatch latch = new CountDownLatch(futureList.size());
        List<Boolean> resultList = new CopyOnWriteArrayList<>();

        getRPCAppendResult(futureList, latch, resultList);

        try {
            latch.await(4000, MILLISECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        for (Boolean aBoolean : resultList) {
            if (aBoolean) {
                success.incrementAndGet();
            }
        }

        // 如果存在一个满足N > commitIndex的 N，并且大多数的matchIndex[i] ≥ N成立，
        // 并且log[N].term == currentTerm成立，那么令 commitIndex 等于这个 N （5.3 和 5.4 节）
        List<Long> matchIndexList = new ArrayList<>(matchIndexs.values());
        // 小于 2, 没有意义
        int median = 0;
        if (matchIndexList.size() >= 2) {
            Collections.sort(matchIndexList);
            median = matchIndexList.size() / 2;
        }
        Long N = matchIndexList.get(median);
        if (N > commitIndex) {
            LogEntry entry = logModule.read(N);
            if (entry != null && entry.getTerm() == currentTerm) {
                commitIndex = N;  // TODO commitIndex 之后没用到，更新这个值的目的是什么？（心跳？复制？代表本轮更新的整体进度？）
            }
        }

        //  响应客户端(成功一半)
        if (success.get() >= (count / 2)) {
            // 更新
            commitIndex = logEntry.getIndex(); // TODO 另一半失败的不管了？
            //  应用到状态机
            getStateMachine().apply(logEntry);
            lastApplied = commitIndex;

            log.info("success apply local state machine,  logEntry info : {}", logEntry);
            // 返回成功.
            return ClientKVAck.ok();
        } else {
            // 回滚已经提交的日志.
            logModule.removeOnStartIndex(logEntry.getIndex());
            log.warn("fail apply local state  machine,  logEntry info : {}", logEntry);
            // TODO 不应用到状态机,但已经记录到日志中.由定时任务从重试队列取出,然后重复尝试,当达到条件时,应用到状态机.
            // 这里应该返回错误, 因为没有成功复制过半机器.
            return ClientKVAck.fail();
        }
    }

    private void getRPCAppendResult(List<Future<Boolean>> futureList, CountDownLatch latch, List<Boolean> resultList) {
        for (Future<Boolean> future : futureList) {
            RaftThreadPool.execute(() -> {
                try {
                    resultList.add(future.get(3000, MILLISECONDS));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    resultList.add(false);
                } finally {
                    latch.countDown();
                }
            });
        }
    }


    /**
     * 复制到其他机器
     */
    public Future<Boolean> replication(Peer peer, LogEntry entry) {

        return RaftThreadPool.submit(() -> {

            long start = System.currentTimeMillis(), end = start;

            // 20 秒重试时间
            while (end - start < 20 * 1000L) {

                AentryParam aentryParam = AentryParam.builder().build();
                aentryParam.setTerm(currentTerm);
                aentryParam.setServerId(peer.getAddr());
                aentryParam.setLeaderId(peerSet.getSelf().getAddr());

                aentryParam.setLeaderCommit(commitIndex);

                // 以我这边为准, 这个行为通常是成为 leader 后,首次进行 RPC 才有意义.
                Long nextIndex = nextIndexs.get(peer);
                LinkedList<LogEntry> logEntries = new LinkedList<>();
                if (entry.getIndex() >= nextIndex) {
                    for (long i = nextIndex; i <= entry.getIndex(); i++) {
                        LogEntry l = logModule.read(i);
                        if (l != null) {
                            logEntries.add(l);
                        }
                    }
                } else {
                    logEntries.add(entry); // TODO follower 的日志比 leader 发的新，不用删除多出来的？
                }
                // 最小的那个日志.
                LogEntry preLog = getPreLog(logEntries.getFirst());
                aentryParam.setPreLogTerm(preLog.getTerm());
                aentryParam.setPrevLogIndex(preLog.getIndex());

                aentryParam.setEntries(logEntries.toArray(new LogEntry[0]));

                Request request = Request.builder()
                        .cmd(Request.A_ENTRIES)
                        .obj(aentryParam)
                        .url(peer.getAddr())
                        .build();

                try {
                    AentryResult result = getRpcClient().send(request);
                    if (result == null) {
                        return false;
                    }
                    if (result.isSuccess()) {
                        log.info("append follower entry success , follower=[{}], entry=[{}]", peer, aentryParam.getEntries());
                        // update 这两个追踪值
                        nextIndexs.put(peer, entry.getIndex() + 1);
                        matchIndexs.put(peer, entry.getIndex());
                        return true;
                    } else if (!result.isSuccess()) {
                        // 对方比我大
                        if (result.getTerm() > currentTerm) {
                            log.warn("follower [{}] term [{}] than more self, and my term = [{}], so, I will become follower",
                                    peer, result.getTerm(), currentTerm);
                            currentTerm = result.getTerm();
                            // 认怂, 变成跟随者
                            status = NodeStatus.FOLLOWER;
                            return false;
                        } // 没我大, 却失败了,说明 index 不对.或者 term 不对.
                        else {
                            // 递减
                            if (nextIndex == 0) {
                                nextIndex = 1L;
                            }
                            nextIndexs.put(peer, nextIndex - 1); // TODO 这块处理好像有点问题，每次退一步有点慢
                            log.warn("follower {} nextIndex not match, will reduce nextIndex and retry RPC append, nextIndex : [{}]", peer.getAddr(),
                                    nextIndex);
                            // 重来, 直到成功.  TODO 重试机制是咋实现的？
                        }
                    }

                    end = System.currentTimeMillis();

                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    // TODO 到底要不要放队列重试?
//                        ReplicationFailModel model =  ReplicationFailModel.newBuilder()
//                            .callable(this)
//                            .logEntry(entry)
//                            .peer(peer)
//                            .offerTime(System.currentTimeMillis())
//                            .build();
//                        replicationFailQueue.offer(model);
                    return false;
                }
            }
            // 超时了,没办法了
            return false;
        });

    }

    private LogEntry getPreLog(LogEntry logEntry) {
        LogEntry entry = logModule.read(logEntry.getIndex() - 1);

        if (entry == null) {
            log.warn("get perLog is null , parameter logEntry : {}", logEntry);
            entry = LogEntry.builder().index(0L).term(0).command(null).build();
        }
        return entry;
    }

    /**
     * 用途：该类用于处理复制失败的任务队列，尝试通过线程池重试执行复制任务，确保分布式系统中的一致性和数据同步。
     * 特点：
     *  支持定时任务消费。
     *  确保任务只在 Leader 节点上执行，避免无效操作。
     *  异常处理和日志记录增强了系统的可观测性与可靠性。
     */
    class ReplicationFailQueueConsumer implements Runnable {

        /**
         * 定义任务重试的时间间隔（1分钟）
         */
        long intervalTime = 1000 * 60;

        /**
         * 核心运行方法，负责消费复制失败队列中的任务，并尝试重新执行复制任务
         */
        @Override
        public void run() {
            // 循环运行，直到外部控制变量 `running` 被设置为 false
            while (running) {
                try {
                    // 从复制失败队列中获取任务，设置超时时间为 1000 毫秒
                    ReplicationFailModel model = replicationFailQueue.poll(1000, MILLISECONDS);

                    // 如果没有任务（队列为空），继续等待
                    if (model == null) {
                        continue;
                    }

                    // 如果当前节点状态不是 Leader，则清空队列，跳过处理
                    if (status != NodeStatus.LEADER) {
                        replicationFailQueue.clear(); // 清空队列
                        continue;
                    }

                    // 打印日志，表示从失败队列中取出任务，并准备重试
                    log.warn("replication Fail Queue Consumer take a task, will be retry replication, content detail : [{}]", model.logEntry);

                    // 检查任务进入队列的时间与当前时间的差值
                    long offerTime = model.offerTime;
                    if (System.currentTimeMillis() - offerTime > intervalTime) {
                        // 如果任务在队列中等待的时间超过了指定的时间间隔，打印警告日志
                        log.warn("replication Fail event Queue maybe full or handler slow");
                    }

                    // 从任务模型中获取可调用的重试逻辑
                    Callable callable = model.callable;

                    // 提交任务到 Raft 的线程池执行，返回 Future 对象以获取执行结果
                    Future<Boolean> future = RaftThreadPool.submit(callable);

                    // 设置任务的最大等待时间为 3000 毫秒，获取任务执行结果
                    Boolean r = future.get(3000, MILLISECONDS);

                    // 如果重试成功，则可能需要应用到状态机
                    if (r) {
                        tryApplyStateMachine(model); // 调用状态机应用逻辑
                    }

                } catch (InterruptedException e) {
                    // 如果线程被中断，忽略异常并继续运行
                } catch (ExecutionException | TimeoutException e) {
                    // 如果任务执行异常或超时，打印警告日志
                    log.warn(e.getMessage());
                }
            }
        }
    }


    private void tryApplyStateMachine(ReplicationFailModel model) {

        String success = stateMachine.getString(model.successKey);
        stateMachine.setString(model.successKey, String.valueOf(Integer.parseInt(success) + 1));

        String count = stateMachine.getString(model.countKey);

        if (Integer.parseInt(success) >= Integer.parseInt(count) / 2) {
            stateMachine.apply(model.logEntry);
            stateMachine.delString(model.countKey, model.successKey);
        }
    }


    @Override
    public void destroy() throws Throwable {
        rpcServer.destroy();
        stateMachine.destroy();
        rpcClient.destroy();
        running = false;
        log.info("destroy success");
    }


    /**
     * 1. 在转变成候选人后就立即开始选举过程
         * 自增当前的任期号（currentTerm）
         * 给自己投票
         * 重置选举超时计时器
         * 发送请求投票的 RPC 给其他所有服务器
     * 2. 如果接收到大多数服务器的选票，那么就变成领导人
     * 3. 如果接收到来自新的领导人的附加日志 RPC，转变成跟随者
     * 4. 如果选举过程超时，再次发起一轮选举
     */
    class ElectionTask implements Runnable {

        @Override
        public void run() {

            // 如果当前节点已经是 Leader，则不需要发起选举，直接返回
            if (status == NodeStatus.LEADER) {
                return;
            }

            // 获取当前时间
            long current = System.currentTimeMillis();

            // 增加随机时间以解决选举冲突（RAFT 中的随机超时时间）
            electionTime = electionTime + ThreadLocalRandom.current().nextInt(50);

            // 如果当前时间减去上次选举的时间小于随机选举时间，则暂不发起选举
            if (current - preElectionTime < electionTime) {
                return;
            }

            // 将节点状态设置为候选者
            status = NodeStatus.CANDIDATE;
            log.error("node {} will become CANDIDATE and start election leader, current term : [{}], LastEntry : [{}]",
                    peerSet.getSelf(), currentTerm, logModule.getLast());

            // 设置下一次选举的时间，加随机值（避免节点同时发起选举导致冲突）
            preElectionTime = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;

            // 自增当前的任期
            currentTerm = currentTerm + 1;

            // 自己投自己一票
            votedFor = peerSet.getSelf().getAddr();

            // 获取集群中除自己以外的所有节点
            List<Peer> peers = peerSet.getPeersWithOutSelf();

            // 保存每个节点的选票结果
            ArrayList<Future<RvoteResult>> futureArrayList = new ArrayList<>();

            log.info("peerList size : {}, peer list content : {}", peers.size(), peers);

            // 向其他节点发送选票请求
            for (Peer peer : peers) {
                // 提交选票请求到线程池执行
                futureArrayList.add(RaftThreadPool.submit(() -> {
                    long lastTerm = 0L;
                    // 获取日志中最后一条记录的任期
                    LogEntry last = logModule.getLast();
                    if (last != null) {
                        lastTerm = last.getTerm();
                    }

                    // 构建选票请求参数
                    RvoteParam param = RvoteParam.builder()
                            .term(currentTerm)
                            .candidateId(peerSet.getSelf().getAddr())
                            .lastLogIndex(LongConvert.convert(logModule.getLastIndex()))
                            .lastLogTerm(lastTerm)
                            .build();

                    // 构建 RPC 请求
                    Request request = Request.builder()
                            .cmd(Request.R_VOTE)
                            .obj(param)
                            .url(peer.getAddr())
                            .build();

                    try {
                        // 发送 RPC 请求
                        return getRpcClient().<RvoteResult>send(request);
                    } catch (RaftRemotingException e) {
                        // 记录 RPC 失败日志
                        log.error("ElectionTask RPC Fail , URL : " + request.getUrl());
                        return null;
                    }
                }));
            }

            // 用于统计成功的选票数量
            AtomicInteger success2 = new AtomicInteger(0);

            // 倒计时锁，用于等待所有选票结果
            CountDownLatch latch = new CountDownLatch(futureArrayList.size());

            log.info("futureArrayList.size() : {}", futureArrayList.size());

            // 处理每个选票的返回结果
            for (Future<RvoteResult> future : futureArrayList) {
                RaftThreadPool.submit(() -> {
                    try {
                        // 等待选票结果，超时时间为 3000 毫秒
                        RvoteResult result = future.get(3000, MILLISECONDS);
                        if (result == null) {
                            return -1;
                        }

                        // 如果选票被授予
                        if (result.isVoteGranted()) {
                            success2.incrementAndGet(); // 成功票数加一
                        } else {
                            // 如果选票未被授予，可能需要更新当前任期
                            long resTerm = result.getTerm();
                            if (resTerm >= currentTerm) {
                                currentTerm = resTerm;
                            }
                        }
                        return 0;
                    } catch (Exception e) {
                        log.error("future.get exception , e : ", e);
                        return -1;
                    } finally {
                        // 选票处理完成，倒计时锁减一
                        latch.countDown();
                    }
                });
            }

            try {
                // 等待所有选票结果完成，最多等待 3500 毫秒
                latch.await(3500, MILLISECONDS);
            } catch (InterruptedException e) {
                log.warn("InterruptedException By Master election Task");
            }

            // 获取成功票数
            int success = success2.get();
            log.info("node {} maybe become leader , success count = {} , status : {}", peerSet.getSelf(), success, NodeStatus.Enum.value(status));

            // 如果在选举期间状态变为 FOLLOWER，则停止后续逻辑
            if (status == NodeStatus.FOLLOWER) {
                return;
            }

            // 如果成功票数超过集群节点的一半，成为 Leader
            if (success >= peers.size() / 2) {
                log.warn("node {} become leader ", peerSet.getSelf());
                status = NodeStatus.LEADER; // 设置节点状态为 LEADER
                peerSet.setLeader(peerSet.getSelf()); // 设置自己为 Leader
                votedFor = ""; // 清空已投票信息
                becomeLeaderToDoThing(); // 执行成为 Leader 后的逻辑
            } else {
                // 如果选举失败，清空已投票信息，准备重新选举
                votedFor = "";
            }

            // 再次更新选举时间，加随机值
            preElectionTime = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;
        }
    }


    /**
     * 初始化所有的 nextIndex 值为自己的最后一条日志的 index + 1. 如果下次 RPC 时, 跟随者和leader 不一致,就会失败.
     * 那么 leader 尝试递减 nextIndex 并进行重试.最终将达成一致.
     */
    private void becomeLeaderToDoThing() {
        nextIndexs = new ConcurrentHashMap<>();
        matchIndexs = new ConcurrentHashMap<>();
        for (Peer peer : peerSet.getPeersWithOutSelf()) {
            nextIndexs.put(peer, logModule.getLastIndex() + 1);
            matchIndexs.put(peer, 0L);
        }

        // 创建[空日志]并提交，用于处理前任领导者未提交的日志
        LogEntry logEntry = LogEntry.builder()
                .command(null)
                .term(currentTerm)
                .build();

        // 预提交到本地日志, TODO 预提交
        logModule.write(logEntry);
        log.info("write logModule success, logEntry info : {}, log index : {}", logEntry, logEntry.getIndex());

        final AtomicInteger success = new AtomicInteger(0);

        List<Future<Boolean>> futureList = new ArrayList<>();

        int count = 0;
        //  复制到其他机器
        for (Peer peer : peerSet.getPeersWithOutSelf()) {
            // TODO check self and RaftThreadPool
            count++;
            // 并行发起 RPC 复制.
            futureList.add(replication(peer, logEntry));
        }

        CountDownLatch latch = new CountDownLatch(futureList.size());
        List<Boolean> resultList = new CopyOnWriteArrayList<>();

        getRPCAppendResult(futureList, latch, resultList);

        try {
            latch.await(4000, MILLISECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        for (Boolean aBoolean : resultList) {
            if (aBoolean) {
                success.incrementAndGet();
            }
        }

        // 如果存在一个满足N > commitIndex的 N，并且大多数的matchIndex[i] ≥ N成立，
        // 并且log[N].term == currentTerm成立，那么令 commitIndex 等于这个 N （5.3 和 5.4 节）
        List<Long> matchIndexList = new ArrayList<>(matchIndexs.values());
        // 小于 2, 没有意义
        int median = 0;
        if (matchIndexList.size() >= 2) {
            Collections.sort(matchIndexList);
            median = matchIndexList.size() / 2;
        }
        Long N = matchIndexList.get(median);
        if (N > commitIndex) {
            LogEntry entry = logModule.read(N);
            if (entry != null && entry.getTerm() == currentTerm) {
                commitIndex = N;
            }
        }

        //  响应客户端(成功一半)
        if (success.get() >= (count / 2)) {
            // 更新
            commitIndex = logEntry.getIndex();
            //  应用到状态机
            getStateMachine().apply(logEntry);
            lastApplied = commitIndex;

            log.info("success apply local state machine,  logEntry info : {}", logEntry);
        } else {
            // 回滚已经提交的日志
            logModule.removeOnStartIndex(logEntry.getIndex());
            log.warn("fail apply local state  machine,  logEntry info : {}", logEntry);

            // 无法提交空日志，让出领导者位置
            log.warn("node {} becomeLeaderToDoThing fail ", peerSet.getSelf());
            status = NodeStatus.FOLLOWER;
            peerSet.setLeader(null);
            votedFor = "";
        }

    }

    /**
     * 心跳机制的作用
         * 维持领导权：心跳包可以告知跟随者节点当前的领导者仍然有效，从而避免跟随者发起选举。
         * 日志同步：通过心跳包中的 leaderCommit，领导者可以通知跟随者同步已提交的日志。
         * 故障检测：如果跟随者没有在规定时间内收到心跳，会认为领导者失效并发起选举。
     */
    class HeartBeatTask implements Runnable {

        @Override
        public void run() {

            // 如果当前节点的状态不是 LEADER（领导者），则不执行心跳任务
            if (status != NodeStatus.LEADER) {
                return;
            }

            // 获取当前时间
            long current = System.currentTimeMillis();

            // 如果当前时间距离上一次发送心跳的时间小于心跳间隔（heartBeatTick），则不发送心跳
            if (current - preHeartBeatTime < heartBeatTick) {
                return;
            }

            // 打印日志，显示每个节点的 nextIndex 值（用于日志复制的下一个索引）
            log.info("=========== NextIndex =============");
            for (Peer peer : peerSet.getPeersWithOutSelf()) {
                log.info("Peer {} nextIndex={}", peer.getAddr(), nextIndexs.get(peer));
            }

            // 更新上一次心跳的时间戳
            preHeartBeatTime = System.currentTimeMillis();

            // 心跳只包含当前任期（term）和领导者 ID（leaderId）
            for (Peer peer : peerSet.getPeersWithOutSelf()) {
                // 构造心跳包参数
                AentryParam param = AentryParam.builder()
                        .entries(null) // 心跳中不包含日志条目，表示为空日志
                        .leaderId(peerSet.getSelf().getAddr()) // 当前领导者的地址
                        .serverId(peer.getAddr()) // 目标跟随者节点的地址
                        .term(currentTerm) // 当前任期
                        .leaderCommit(commitIndex) // 向跟随者同步已提交的日志索引
                        .build();

                // 构造 RPC 请求
                Request request = new Request(
                        Request.A_ENTRIES, // 请求类型：AppendEntries
                        param,             // 请求参数
                        peer.getAddr()     // 跟随者节点地址
                );

                // 将心跳任务提交到线程池异步执行
                RaftThreadPool.execute(() -> {
                    try {
                        // 发送心跳请求并获取响应结果
                        AentryResult aentryResult = getRpcClient().send(request);

                        // 获取响应中的任期
                        long term = aentryResult.getTerm();

                        // 如果响应中的任期比当前节点的任期大，说明出现了更高任期的领导者
                        if (term > currentTerm) {
                            log.error("self will become follower, he's term : {}, my term : {}", term, currentTerm);
                            currentTerm = term; // 更新当前节点的任期
                            votedFor = ""; // 清空已投票的记录
                            status = NodeStatus.FOLLOWER; // 切换节点状态为 FOLLOWER（跟随者）
                        }
                    } catch (Exception e) {
                        // 如果心跳请求失败，记录错误日志
                        log.error("HeartBeatTask RPC Fail, request URL : {} ", request.getUrl());
                    }
                }, false); // 第二个参数 false 表示异步执行，不阻塞主线程
            }
        }
    }


    @Override
    public Result addPeer(Peer newPeer) {
        return delegate.addPeer(newPeer);
    }

    @Override
    public Result removePeer(Peer oldPeer) {
        return delegate.removePeer(oldPeer);
    }

}
