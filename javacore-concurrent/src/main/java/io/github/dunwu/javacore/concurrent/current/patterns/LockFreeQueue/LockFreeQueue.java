package io.github.dunwu.javacore.concurrent.current.patterns.LockFreeQueue;


import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class LockFreeQueue<E> {
    //定义头和尾的原子性节点
    private AtomicReference<Node<E>> head, last;
    //定义原子性size
    private AtomicInteger size = new AtomicInteger(0);
    //初始化队列，将头和尾都指向一个null的节点
    public LockFreeQueue() {
        Node<E> node = new Node<E>(null);
        head = new AtomicReference<Node<E>>(node);
        last = new AtomicReference<Node<E>>(node);
    }
    //定义节点类
    private static class Node<E> {
        E element;
        //需要volatile，因为防止在next赋值的时候发生重排序，并且需要对其他线程可见
        volatile Node<E> next;

        public Node(E element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return element == null ? null : element.toString();
        }
    }
    //添加元素到队尾
    public void addLast(E element) {
        //元素不允许为null
        if (element == null)
            throw new NullPointerException("The null element not allow");
        //新建一个新节点
        Node<E> newNode = new Node<E>(element);
        //getAndSet操作为原子性操作，先获取last的节点再将新的节点赋值给last
        Node<E> oldNode = last.getAndSet(newNode);
        //将旧节点的next指向新的节点
        oldNode.next = newNode;
        //队列长度+1
        size.incrementAndGet();
    }
    //移除并返回队首元素
    public E removeFirst() {
        //因为队首节点是存在的，但是他可能没有下一个节点，所以需要一个valueNode来判断
        Node<E> headNode, valueNode;
        do {
            //获取到队首节点
            headNode = head.get();
            //判断下一个节点是否为null
            valueNode = headNode.next;
            //当valueNode不为null，并且headNode不等于队列的head节点时，代表该元素被别的线程拿走的，需要重新获取。
            //当headNode等于队列的head时则代表头元素没有被其他元素拿走，并将head节点替换为valueNode。
        } while (valueNode != null && !head.compareAndSet(headNode, valueNode));

        E result = valueNode != null ? valueNode.element : null;
        //valueNode的元素被拿走了，所有将其置为null
        if (valueNode != null) {
            valueNode.element = null;
        }
        //队列长度-1
        size.decrementAndGet();
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        //创建线程池
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        //实例化队列
        LockFreeQueue<String> queue = new LockFreeQueue<String>();
        //该map用于检查该队列是否是线程安全的，利用其key不能重复来判断
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        //随机数
        Random random = new Random(System.currentTimeMillis());

        //创建5个写runnable
        IntStream.range(0, 5).boxed().map(i -> (Runnable) () -> {
            int count = 0;
            //每个runnable往队列插入10个元素
            while (count++<10) {
                //这里值用系统的纳秒+随机数+count，以防止重复影响map集合对队列线程安全的判断
                queue.addLast(System.nanoTime()+":"+random.nextInt(10000)+":"+count);
            }
            //提交任务
        }).forEach(threadPool::submit);
        //创建5个读runnable
        IntStream.range(0, 5).boxed().map(i -> (Runnable) () -> {
            int count = 10;
            //每个runnable读10个元素
            while (count-->0) {
                //休眠
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //移除队列中的队首元素
                String result = queue.removeFirst();
                //输出
                System.out.println(result);
                //将该元素加入map中，来判断队列中真实存入的元素个数
                map.put(result, new Object());
            }
            //提交任务
        }).forEach(threadPool::submit);

        //关闭线程池
        threadPool.shutdown();
        //等待1小时候强制关闭线程池
        threadPool.awaitTermination(1, TimeUnit.HOURS);
        //打印map中的元素个数
        System.out.println(map.size());
    }
}
