package com.example.concurrency.completable_future.complete_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * 适用场景：
 * 当任务执行时间受限时，可以使用 orTimeout 方法为任务设置超时，确保程序不会因任务超时而被阻塞。
 *
 * 注意事项：
 * orTimeout 方法不会中断正在执行的任务，只是为任务提供一个超时检查机制。
 * 即使任务超时，任务的原始逻辑仍然会继续运行，直到自然完成或被其他操作中断。
 */
public class OrTimeout extends Demo {

    @Test
    public void getNow() throws InterruptedException, ExecutionException {
        // 创建一个异步任务，模拟耗时操作 2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"))
                // 设置任务的超时时间为 3 秒，若任务未在超时之前完成，会抛出 TimeoutException
                .orTimeout(3, TimeUnit.SECONDS);

        // 验证任务在超时之前完成，返回结果为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void getNowValueIfAbsent() throws InterruptedException {
        // 创建一个异步任务，模拟耗时操作 2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"))
                // 设置任务的超时时间为 1 秒，若任务未在超时之前完成，会抛出 TimeoutException
                .orTimeout(1, TimeUnit.SECONDS);

        try {
            // 尝试获取任务结果
            future.get();
            // 如果未抛出异常，则测试失败
            fail();
        } catch (ExecutionException e) {
            // 捕获 ExecutionException，验证其根本原因是 TimeoutException
            assertEquals(TimeoutException.class, e.getCause().getClass());
        }
    }
}

