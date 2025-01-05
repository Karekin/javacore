package com.example.concurrency.completable_future.check_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 使用 isCancelled() 方法判断任务是否被取消，以便执行相应的补偿逻辑或记录任务状态。
 * cancel() 方法适合需要中断长时间运行任务的场景。
 */
public class IsCancelled extends Demo {

    @Test
    public void testIsCancelledTrue() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证 future 没有被取消
        assertFalse(future.isCancelled());
        // 验证 future 的结果是否为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void testIsCancelledFalse() {
        // 使用 supplyAsync 创建一个异步任务，任务的结果为 sleepAndGet("value")
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

        // 验证 future 尚未完成
        assertFalse(future.isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证 future 没有被取消
        assertFalse(future.isCancelled());

        // 取消 future，传递参数 true 表示允许任务中断
        future.cancel(true);

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 是以异常形式完成的（取消视为异常）
        assertTrue(future.isCompletedExceptionally());
        // 验证 future 已被取消
        assertTrue(future.isCancelled());
    }
}
