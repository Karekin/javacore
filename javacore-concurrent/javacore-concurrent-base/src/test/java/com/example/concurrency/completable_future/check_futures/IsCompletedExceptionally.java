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
 * 通过 isCompletedExceptionally() 方法，可以判断任务是否以异常形式完成，便于异常处理或记录日志。
 * 在调试或单元测试中，用于验证任务状态是否符合预期，尤其是异常情况下的处理逻辑。
 */
public class IsCompletedExceptionally extends Demo {

    @Test
    public void testIsCompletedExceptionallyFalse() throws InterruptedException, ExecutionException {
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
    public void testIsCompletedExceptionallyTrue() {
        // 使用 failedFuture 创建一个以异常形式完成的 CompletableFuture
        // 异常为 RuntimeException("exception")
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException("exception"));

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 是以异常形式完成的
        assertTrue(future.isCompletedExceptionally());
        // 验证 future 没有被取消
        assertFalse(future.isCancelled());
    }
}

