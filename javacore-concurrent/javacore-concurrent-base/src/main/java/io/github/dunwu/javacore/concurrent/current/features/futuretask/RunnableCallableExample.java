package io.github.dunwu.javacore.concurrent.current.features.futuretask;

import java.util.concurrent.*;

public class RunnableCallableExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 运行 Runnable 示例
        System.out.println("Running Runnable example...");
        runRunnableExample();

        // 运行 Callable 示例
        System.out.println("\nRunning Callable example...");
        runCallableExample();
    }

    // Runnable 示例方法
    private static void runRunnableExample() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("This is a Runnable task");
            }
        };

        Thread thread = new Thread(task);
        thread.start();  // 启动线程执行 Runnable 任务
        try {
            thread.join();  // 等待线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Callable 示例方法
    private static void runCallableExample() throws InterruptedException, ExecutionException {
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("This is a Callable task");
                return 123;  // 返回结果
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);  // 提交 Callable 任务

        // 获取任务执行的结果
        Integer result = future.get();
        System.out.println("Task result: " + result);

        executor.shutdown();
    }
}

