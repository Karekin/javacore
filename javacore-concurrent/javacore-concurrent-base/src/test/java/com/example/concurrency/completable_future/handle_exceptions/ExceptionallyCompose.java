package com.example.concurrency.completable_future.handle_exceptions;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * 适用场景：
 * 当任务可能发生异常时，使用 exceptionallyCompose 替代异常的任务，提供默认值或替代任务逻辑。
 * 与 exceptionally 不同，exceptionallyCompose 支持返回一个新的异步任务，更灵活地处理异常场景。
 */
public class ExceptionallyCompose extends Demo {

    @Test
    public void testExceptionallySuccess() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，结果为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value")
                // 使用 exceptionallyCompose 方法，当任务正常完成时，不会触发回调
                .exceptionallyCompose(t -> CompletableFuture.completedFuture("failure: " + t.getMessage()));

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证最终结果是否为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void testExceptionallyError() throws InterruptedException, ExecutionException {
        // 创建一个已失败的 CompletableFuture，抛出 RuntimeException("exception")
        CompletableFuture<String> future = CompletableFuture.<String>failedFuture(new RuntimeException("exception"))
                // 使用 exceptionallyCompose 方法，当任务失败时，触发回调
                // 在回调中处理异常，并返回一个包含错误信息的新 CompletableFuture
                .exceptionallyCompose(t -> CompletableFuture.completedFuture("failure: " + t.getMessage()));

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常形式完成，因为异常已被处理
        assertFalse(future.isCompletedExceptionally());
        // 验证最终结果是否为 "failure: exception"
        assertEquals("failure: exception", future.get());
    }
}

