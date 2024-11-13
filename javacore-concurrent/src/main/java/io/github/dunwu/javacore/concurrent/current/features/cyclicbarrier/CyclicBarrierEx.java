package io.github.dunwu.javacore.concurrent.current.features.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * 描述:
 * CyclicBarrierEx 中文意思 篱栅 是一组线程之间互相等待的工具类。
 * CyclicBarrier 用于协调一组线程相互等待，直到所有线程都到达某个同步点后，才能继续执行。
 * 本例通过模拟多个线程执行任务，演示了 CyclicBarrier 的使用。
 *
 * @author zed
 * @since 2019-06-18 10:27 AM
 */
public class CyclicBarrierEx {

    /**
     * 自定义工作线程类
     * 每个工作线程在启动后，会先等待其他线程达到屏障点（CyclicBarrier.await()），
     * 所有线程都到达屏障点后，才开始执行后续任务。
     */
    private static class Worker extends Thread {
        private final CyclicBarrier cyclicBarrier; // 通过构造方法传入 CyclicBarrier

        // 构造方法，传入 CyclicBarrier 对象
        public Worker(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                // 输出当前线程等待其他线程的消息
                System.out.println(Thread.currentThread().getName() + "开始等待其他线程");

                // 当前线程在此处等待，直到其他线程调用 await() 方法
                cyclicBarrier.await(); // 等待其他线程达到屏障点

                // 输出当前线程开始执行的消息
                System.out.println(Thread.currentThread().getName() + "开始执行");

                // 模拟线程执行任务，这里用 Thread.sleep() 来模拟业务处理
                Thread.sleep(1000);

                // 输出当前线程执行完毕的消息
                System.out.println(Thread.currentThread().getName() + "执行完毕");

            } catch (Exception e) {
                // 如果发生异常，打印异常信息
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 线程数量设置为 3
        int threadCount = 3;

        // 创建一个 CyclicBarrier 对象，屏障点为 3，表示当 3 个线程都到达屏障点后，才继续执行
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);

        // 创建并启动 3 个工作线程
        for (int i = 0; i < threadCount; i++) {
            System.out.println("创建工作线程" + i);

            // 创建一个 Worker 线程，并传入 CyclicBarrier
            Worker worker = new Worker(cyclicBarrier);

            // 启动线程
            worker.start();
        }
    }
}
