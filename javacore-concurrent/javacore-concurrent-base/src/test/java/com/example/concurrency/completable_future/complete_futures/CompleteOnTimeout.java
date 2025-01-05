package com.example.concurrency.completable_future.complete_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * 适用场景：
 * 适合在超时敏感的场景中使用，例如网络请求或后台任务，防止任务因长时间未完成而阻塞。
 *
 * 注意事项：
 * completeOnTimeout 方法不会中断正在执行的任务，只是为任务提供了一个超时完成的逻辑。
 * 任务的原始逻辑仍然会继续运行，直到自然完成。
 */
public class CompleteOnTimeout extends Demo {

    @Test
    public void testCompleteOnTimeout1() throws InterruptedException, ExecutionException {
        // 创建一个异步任务，模拟耗时操作 1 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "value"))
                // 如果任务未在 2 秒内完成，则自动完成任务并设置结果为 "default"
                .completeOnTimeout("default", 2, TimeUnit.SECONDS);

        // 验证任务在超时之前完成，结果为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void testCompleteOnTimeout2() throws InterruptedException, ExecutionException {
        // 创建一个异步任务，模拟耗时操作 2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"))
                // 如果任务未在 1 秒内完成，则自动完成任务并设置结果为 "default"
                .completeOnTimeout("default", 1, TimeUnit.SECONDS);

        // 验证任务未能在超时之前完成，结果为超时设置的 "default"
        assertEquals("default", future.get());
    }
}

