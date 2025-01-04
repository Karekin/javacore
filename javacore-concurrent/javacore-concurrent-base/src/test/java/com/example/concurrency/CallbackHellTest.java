package com.example.concurrency;

import io.github.dunwu.javacore.concurrent.current.features.callback.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Slf4j
public class CallbackHellTest {

    @Test
    public void imperative() throws ExecutionException, InterruptedException {
        ImperWorker worker = new ImperWorker();
        ImperBoss boss = new ImperBoss(worker);
        boss.makeBigDeals("coding");
    }

    @Test
    public void callback() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Callback<String>() {
            final Worker worker = new Worker();
            @Override
            public void callback(String s) {
                log.info("老板拿到结果： {}", s);
                countDownLatch.countDown();
            }

            void makeBigDeal(String deal) {
                log.info("分配工作...");
                new Thread(() -> worker.workAsync(this, deal), "worker").start();
                log.info("分配完工作。");
                log.info("老板下班回家了。。。。");
            }
        }.makeBigDeal("A big deal");
        countDownLatch.await();
    }

    @Test
    public void callbackHell() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 主线程是boss
        new Callback<String>() {
            private final Worker productManager = new Worker();
            @Override
            public void callback(String s) {
                log.info("老板：拿到结果，交给程序员 {}", s);
                new Thread(() -> {
                    new Callback<String>() {
                        private final Worker coder = new Worker();

                        @Override
                        public void callback(String s) {
                            log.info("程序员：完成任务{}", s);
                            countDownLatch.countDown();
                        }

                        public void coding(String coding) {
                            coder.workAsync(this, coding); // 在这里的this是产品，所以回调给产品，如果需要回调给boss这输入的则是boss
                        }
                    }.coding(s);
                }, "coder").start();
            }

            public void makeBigDeals(String bigDeal) {
                log.info("老板：将任务交给产品");
                new Thread(() -> {
                    this.productManager.workAsync(this, bigDeal);
                }, "Product").start();
            }
        }.makeBigDeals("一个大项目");
        log.info("老板：下班回家");
        countDownLatch.await();
    }

    @Test
    public void callbackHellWithCompletableFuture() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Worker productManager = new Worker();
        Worker coder = new Worker();

        CompletableFuture.supplyAsync(() -> {
            log.info("老板：将任务交给产品");
            return "一个大项目";
        }).thenApplyAsync(bigDeal -> {
            log.info("产品经理：接到任务，开始处理...");
            // 模拟产品经理工作
            productManager.workSync(bigDeal); // 假设有同步的处理方法
            return "产品完成的任务";
        }).thenApplyAsync(productTask -> {
            log.info("程序员：接到任务，开始编码...");
            // 模拟程序员工作
            coder.workSync(productTask); // 假设有同步的处理方法
            return "编码完成的任务";
        }).thenAccept(finalResult -> {
            log.info("老板：拿到最终结果 {}", finalResult);
            countDownLatch.countDown();
        }).exceptionally(ex -> {
            log.error("任务执行失败：{}", ex.getMessage(), ex);
            countDownLatch.countDown();
            return null;
        });

        log.info("老板：下班回家");
        countDownLatch.await();
    }


    @Test
    public void reactive() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.defer(() -> {
                    log.info("老板：将任务交给产品");
                    return Mono.just("项目");
                })
                .publishOn(Schedulers.newSingle("Product"))
                .map(s -> {
                    log.info("产品经理：开始工作");
                    String midResult = "设计(" + s + ")";
                    log.info("产品经理：处理任务并给出原型: " + midResult);
                    log.info("产品经理：将任务交给程序员");
                    return midResult;
                })
                .publishOn(Schedulers.newSingle("Coder"))
                .map(s-> {
                    log.info("程序员：开始工作");
                    String result = "编程(" + s + ")";
                    log.info("程序员：完成任务{}", result);
                    return result;
                }).subscribe(result -> {
                    System.out.println("项目完成：" + result);
                    countDownLatch.countDown();
                });
        log.info("老板：下班回家");
        countDownLatch.await();

    }

}
