package io.github.dunwu.javacore.concurrent.current.features.threadstate;

import io.github.dunwu.javacore.concurrent.current.util.ThreadDumpHelper;
import io.github.dunwu.javacore.concurrent.current.util.ThreadUtil;

/**
 * 描述:
 * 线程状态一览
 * 该程序展示了不同线程状态的示例，包括：
 * 1. TimeWaiting（时间等待）
 * 2. Waiting（无限等待）
 * 3. Blocked（阻塞状态）
 *
 * @author zed
 */
public class ThreadState {

    // 用于生成线程状态转储（Thread Dump）的工具类
    private static final ThreadDumpHelper threadDumpHelper = new ThreadDumpHelper();

    public static void main(String[] args) {

        // 创建并启动线程，分别演示 TimeWaiting、Waiting、Blocked 状态
        new Thread(new TimeWaiting(), "TimeWaiting").start();  // 时间等待状态
        new Thread(new Waiting(), "Waiting").start();  // 等待状态
        new Thread(new Blocked(), "Blocked1").start();  // 阻塞状态
        new Thread(new Blocked(), "Blocked2").start();  // 阻塞状态

        // 获取当前所有线程的状态转储
        threadDumpHelper.tryThreadDump();
    }

    /**
     * 线程处于 TimeWaiting（时间等待）状态，通常通过调用 Thread.sleep() 或者
     * Object.wait(long) 方法进入该状态，线程会进入睡眠一段时间。
     */
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                // 线程休眠 1000 毫秒（即 1 秒），进入时间等待状态
                ThreadUtil.sleep(1000);
            }
        }
    }

    /**
     * 线程处于 Waiting（无限等待）状态，通常是通过调用 Object.wait() 方法
     * 进入该状态，线程会一直等待直到其他线程唤醒它（调用 notify() 或 notifyAll()）。
     */
    static class Waiting implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        // 线程获取 Waiting.class 锁后进入等待状态
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        // 线程被中断时恢复中断状态
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    /**
     * 线程处于 Blocked（阻塞）状态，通常是由于线程需要获取锁但被其他线程持有
     * 时，线程进入阻塞状态。该状态的线程会在锁释放后重新获取 CPU 执行。
     */
    static class Blocked implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (Blocked.class) {
                    // 线程获取 Blocked.class 锁后执行，模拟线程的阻塞状态
                    ThreadUtil.sleep(1000);
                }
            }
        }
    }
}
