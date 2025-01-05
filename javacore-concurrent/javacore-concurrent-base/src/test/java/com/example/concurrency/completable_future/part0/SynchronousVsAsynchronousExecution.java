package com.example.concurrency.completable_future.part0;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 比较同步与异步执行方式的示例代码。
 * 主要展示三种情况：同步执行、基于 Future 的异步执行、基于 CompletableFuture 的异步执行。
 */
public class SynchronousVsAsynchronousExecution extends Demo {

    /**
     * 演示同步执行。
     * 所有任务按顺序阻塞执行，后续任务必须等待前一个任务完成。
     */
    @Test
    public void testSynchronous() {
        logger.info("this task started");

        // 阻塞获取价格和汇率
        int netAmountInUsd = getPriceInEur() * getExchangeRateEurToUsd(); // 阻塞执行
        // 阻塞计算税费
        float tax = getTax(netAmountInUsd); // 阻塞执行
        // 计算总金额
        float grossAmountInUsd = netAmountInUsd * (1 + tax);

        logger.info("this task finished: {}", grossAmountInUsd);

        // 模拟其他任务开始
        logger.info("another task started");
    }

    /**
     * 演示基于 Future 的异步执行。
     * 使用线程池异步执行任务，避免阻塞主线程，但仍需手动控制任务依赖和结果获取。
     */
    @Test
    public void testAsynchronousWithFuture() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        logger.info("this task started");

        // 提交异步任务，分别获取价格和汇率
        Future<Integer> priceInEur = executorService.submit(this::getPriceInEur);
        Future<Integer> exchangeRateEurToUsd = executorService.submit(this::getExchangeRateEurToUsd);

        // 主线程轮询任务完成状态，避免阻塞
        while (!priceInEur.isDone() || !exchangeRateEurToUsd.isDone()) {
            Thread.sleep(100);
            logger.info("another task is running");
        }

        // 获取异步结果并计算净金额
        int netAmountInUsd = priceInEur.get() * exchangeRateEurToUsd.get();
        // 提交异步任务计算税费
        Future<Float> tax = executorService.submit(() -> getTax(netAmountInUsd));

        // 主线程轮询任务完成状态，避免阻塞
        while (!tax.isDone()) {
            Thread.sleep(100);
            logger.info("another task is running");
        }

        // 获取税费并计算总金额
        float grossAmountInUsd = netAmountInUsd * (1 + tax.get());

        logger.info("this task finished: {}", grossAmountInUsd);

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        // 模拟其他任务开始
        logger.info("another task is running");
    }

    /**
     * 演示基于 CompletableFuture 的异步执行。
     * 通过链式调用实现任务依赖，简化了任务控制逻辑，同时保持非阻塞。
     */
    @Test
    public void testAsynchronousWithCompletableFuture() throws InterruptedException {
        // 异步获取价格和汇率
        CompletableFuture<Integer> priceInEur = CompletableFuture.supplyAsync(this::getPriceInEur);
        CompletableFuture<Integer> exchangeRateEurToUsd = CompletableFuture.supplyAsync(this::getExchangeRateEurToUsd);

        // 组合两个异步任务，计算净金额
        CompletableFuture<Integer> netAmountInUsd = priceInEur
                .thenCombine(exchangeRateEurToUsd, (price, exchangeRate) -> price * exchangeRate);

        logger.info("this task started");

        // 链式调用，计算总金额并处理结果或异常
        netAmountInUsd
                .thenCompose(amount -> CompletableFuture.supplyAsync(() -> amount * (1 + getTax(amount))))
                .whenComplete((grossAmountInUsd, throwable) -> {
                    if (throwable == null) {
                        logger.info("this task finished: {}", grossAmountInUsd);
                    } else {
                        logger.warn("this task failed: {}", throwable.getMessage());
                    }
                }); // 非阻塞调用

        // 模拟其他任务开始
        logger.info("another task started");
        Thread.sleep(10000); // 保持主线程运行，等待异步任务完成
    }

    /**
     * 模拟获取价格的方法，模拟耗时 2 秒。
     * @return 返回价格
     */
    private int getPriceInEur() {
        return sleepAndGet(2);
    }

    /**
     * 模拟获取汇率的方法，模拟耗时 4 秒。
     * @return 返回汇率
     */
    private int getExchangeRateEurToUsd() {
        return sleepAndGet(4);
    }

    /**
     * 模拟计算税费的方法，模拟耗时 50 毫秒。
     * @param amount 金额
     * @return 返回税率
     */
    private float getTax(int amount) {
        return sleepAndGet(50) / 100f;
    }
}
