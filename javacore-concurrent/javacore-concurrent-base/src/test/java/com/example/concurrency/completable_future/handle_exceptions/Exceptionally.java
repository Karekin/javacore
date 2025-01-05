package com.example.concurrency.completable_future.handle_exceptions;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * 对比：
 * exceptionally 是处理异常的快捷方式，仅返回替代结果，不支持返回新的异步任务（与 exceptionallyCompose 区别）。
 * 适合处理简单的异常场景，例如记录日志、返回默认值或提供错误信息。
 *
 * 适用场景：
 * 当任务失败时，提供默认值或错误信息作为替代结果。
 * 避免任务以异常形式完成，确保任务链能够继续运行。
 */
public class Exceptionally extends Demo {

    @Test
    public void testExceptionallySuccess() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value")
                // 使用 exceptionally 方法，当任务正常完成时，不会触发回调
                .exceptionally(t -> "failure: " + t.getMessage());

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
                // 使用 exceptionally 方法，当任务失败时，触发回调
                // 回调返回错误信息 "failure: exception"
                .exceptionally(t -> "failure: " + t.getMessage());

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常形式完成，因为异常已被处理
        assertFalse(future.isCompletedExceptionally());
        // 验证最终结果是否为 "failure: exception"
        assertEquals("failure: exception", future.get());
    }
}

