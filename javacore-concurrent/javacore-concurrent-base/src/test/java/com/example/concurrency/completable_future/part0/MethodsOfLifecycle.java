package com.example.concurrency.completable_future.part0;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 适用场景：
 * 需要显式管理 CompletableFuture 的生命周期（如手动完成）。
 * 使用异步任务更新 CompletableFuture 的状态。
 *
 * 注意事项：
 * complete() 是线程安全的，可以在多个线程中调用，但只会执行一次，后续调用会被忽略。
 * 使用循环检查时，应避免频繁检查（如添加休眠）以减少 CPU 占用。
 * 确保 ExecutorService 在任务完成后正确关闭，避免线程泄漏。
 */
public class MethodsOfLifecycle extends Demo {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        // 创建一个单线程的 ExecutorService，用于异步执行任务
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 创建一个未完成的 CompletableFuture
        CompletableFuture<String> future = new CompletableFuture<>();

        // 提交一个异步任务到 executorService
        executorService.submit(() -> {
            // 模拟任务延迟 1 秒
            TimeUnit.SECONDS.sleep(1);
            // 使用 complete 方法完成 CompletableFuture，设置结果为 "value"
            future.complete("value");
            return null;
        });

        // 循环检查 CompletableFuture 是否完成
        while (!future.isDone()) {
            // 如果未完成，主线程休眠 2 秒，避免过度占用 CPU
            TimeUnit.SECONDS.sleep(2);
        }

        // 使用 get 方法读取已完成的 CompletableFuture 的结果
        String result = future.get();
        // 打印结果到日志
        logger.info("result: {}", result);

        // 关闭 ExecutorService，释放线程资源
        executorService.shutdown();
    }
}

