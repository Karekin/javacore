package com.example.concurrency.completable_future.tests;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 适用于需要延迟执行任务的异步场景，例如定时任务、延迟处理等。
 *
 * 注意事项：
 * delayedExecutor 仅控制任务的开始时间，不影响任务的执行时间。
 * 主线程休眠时间应足够长，确保延迟任务有机会完成。
 * 如果延迟时间过短或主线程过早退出，任务可能无法按预期完成。
 */
public class DelayedExecutor extends Demo {

    @Test
    public void testDelayedExecutor() throws InterruptedException, ExecutionException {
        // 创建一个未完成的 CompletableFuture
        CompletableFuture<Object> future = new CompletableFuture<>();
        // 验证 future 尚未完成
        assertFalse(future.isDone());

        // 使用 completeAsync 方法异步完成 CompletableFuture
        // 传入 delayedExecutor，设置延迟 1 秒后执行任务，返回结果 "value"
        future.completeAsync(() -> "value", CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
        // 验证 future 在延迟时间内尚未完成
        assertFalse(future.isDone());

        // 主线程休眠 2 秒，等待异步任务完成
        TimeUnit.SECONDS.sleep(2);

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 的结果为 "value"
        assertEquals("value", future.get());
    }
}

