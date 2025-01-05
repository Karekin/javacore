package com.example.concurrency.completable_future.tests;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 用于调试或特殊场景下强制修改 CompletableFuture 的状态，例如错误恢复或测试。
 *
 * 注意事项：
 * 调用 obtrudeException 会直接修改任务的状态，对其他依赖任务可能产生影响。
 * 应谨慎使用此方法，仅在明确需要强制覆盖结果的场景下使用。
 */
public class ObtrudeException extends Demo {

    @Test
    public void testObtrudeException1() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，其结果为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 验证 future 的状态
        assertTrue(future.isDone()); // 已完成
        assertFalse(future.isCompletedExceptionally()); // 未以异常完成
        assertEquals("value", future.get()); // 返回的结果为 "value"

        // 使用 obtrudeException 方法强制将 CompletableFuture 的结果改为异常
        future.obtrudeException(new RuntimeException("exception"));

        // 验证 future 的状态更新
        assertTrue(future.isDone()); // 已完成
        assertTrue(future.isCompletedExceptionally()); // 以异常完成
    }

    @Test
    public void testObtrudeException2() {
        // 创建一个以异常完成的 CompletableFuture，其异常为 RuntimeException("exception1")
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException("exception1"));

        // 验证 future 的状态
        assertTrue(future.isDone()); // 已完成
        assertTrue(future.isCompletedExceptionally()); // 以异常完成

        // 使用 obtrudeException 方法强制更改 CompletableFuture 的异常为 RuntimeException("exception2")
        future.obtrudeException(new RuntimeException("exception2"));

        // 验证 future 的状态更新
        assertTrue(future.isDone()); // 已完成
        assertTrue(future.isCompletedExceptionally()); // 以异常完成
    }
}

