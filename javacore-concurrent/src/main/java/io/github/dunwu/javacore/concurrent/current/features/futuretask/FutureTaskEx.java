package io.github.dunwu.javacore.concurrent.current.features.futuretask;

import io.github.dunwu.javacore.concurrent.current.features.threadPool.ThreadPoolBuilder;
import io.github.dunwu.javacore.concurrent.current.features.threadPool.ThreadPoolUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * FutureTaskEx 演示了如何使用 FutureTask 来执行并获取异步任务的结果。
 * 本示例包括了通过线程池和独立线程执行 FutureTask、通过 FutureTask 实现烧水泡茶的模拟等。
 *
 * @author zed
 * @since 2019-06-18 4:52 PM
 */
public class FutureTaskEx {

    /**
     * FutureTask 由线程池执行
     * 创建并提交一个 FutureTask 到线程池，执行简单的加法运算并获取结果。
     */
    private static void exeForPool(){
        // 创建一个 FutureTask，执行简单的加法任务 1 + 2
        FutureTask<Integer> futureTask = new FutureTask<>(()-> 1 + 2);

        // 创建线程池
        ThreadPoolExecutor executor = ThreadPoolBuilder.fixedPool().build();

        try {
            // 提交 FutureTask 到线程池执行
            executor.submit(futureTask);

            // 获取并打印任务的执行结果
            Integer result = futureTask.get();
            System.out.println(result);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            // 线程池关闭，使用优雅关闭方法
            ThreadPoolUtil.gracefulShutdown(executor, 1);
        }
    }

    /**
     * FutureTask 由独立线程执行
     * 创建并启动一个线程来执行 FutureTask，并获取结果。
     */
    private static void exeForThread(){
        // 创建一个 FutureTask，执行简单的加法任务 1 + 2
        FutureTask<Integer> futureTask = new FutureTask<>(()-> 1 + 2);

        // 创建并启动一个新线程来执行 FutureTask
        Thread T1 = new Thread(futureTask);
        T1.start();

        try {
            // 获取并打印任务的执行结果
            Integer result = futureTask.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 FutureTask 实现烧水泡茶的过程
     * 模拟两个任务的依赖关系：T1 需要等待 T2 完成后才能继续执行
     */
    private static void fireWater(){
        // 创建 T2 任务的 FutureTask
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());

        // 创建 T1 任务的 FutureTask，其中 T1 依赖于 T2
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));

        // 启动线程执行 T1 任务
        Thread t1 = new Thread(ft1);
        t1.start();

        // 启动线程执行 T2 任务
        Thread t2 = new Thread(ft2);
        t2.start();

        try {
            // 等待并输出 T1 任务的执行结果
            System.out.println(ft1.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * T1 任务：洗水壶、烧开水、泡茶
     * T1 依赖于 T2 完成“拿茶叶”的任务，因此在执行过程中需要等待 T2 完成
     */
    static class T1Task implements Callable<String> {
        FutureTask<String> ft2;

        // 构造函数接受 T2 的 FutureTask，确保 T1 在需要时获取 T2 的结果
        T1Task(FutureTask<String> ft2){
            this.ft2 = ft2;
        }

        @Override
        public String call() throws Exception {
            // 模拟洗水壶的过程
            System.out.println("T1: 洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            // 模拟烧开水的过程
            System.out.println("T1: 烧开水...");
            TimeUnit.SECONDS.sleep(15);

            // 等待并获取 T2 线程的结果，即拿到茶叶
            String tf = ft2.get();
            System.out.println("T1: 拿到茶叶: " + tf);

            // 模拟泡茶的过程
            System.out.println("T1: 泡茶...");
            return "上茶: " + tf;  // 返回上茶的结果，包含茶叶
        }
    }

    /**
     * T2 任务：洗茶壶、洗茶杯、拿茶叶
     * T2 任务独立于 T1 执行，在 T1 等待时执行
     */
    static class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            // 模拟洗茶壶的过程
            System.out.println("T2: 洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            // 模拟洗茶杯的过程
            System.out.println("T2: 洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            // 模拟拿茶叶的过程
            System.out.println("T2: 拿茶叶...");
            TimeUnit.SECONDS.sleep(1);

            // 返回茶叶名称
            return "龙井";
        }
    }

    /**
     * 主方法，演示了 FutureTask 的应用
     * 包括：
     * 1. 使用线程池执行任务
     * 2. 使用独立线程执行任务
     * 3. 使用 FutureTask 实现烧水泡茶的任务依赖
     */
    public static void main(String[] args) {
        // 执行由线程池提交的任务
        exeForPool();

        // 执行由独立线程提交的任务
        exeForThread();

        // 执行烧水泡茶的任务
        fireWater();
    }
}
