package com.example.concurrency.completable_future.create_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 当需要模拟任务失败或直接返回异常完成的任务时，使用 failedFuture 或 failedStage 方法可以快速创建对应实例。
 */
public class FailedFuture extends Demo {

    @Test
    public void testCompletedFuture() {
        // 使用 CompletableFuture.failedFuture 创建一个已失败的 CompletableFuture
        // 该方法直接返回一个异常完成的 CompletableFuture 实例，异常为 RuntimeException("exception")
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException("exception"));

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 是以异常形式完成的
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    public void testCompletedStage() {
        // 使用 CompletableFuture.failedStage 创建一个已失败的 CompletionStage
        // 该方法返回一个异常完成的 CompletionStage 实例，异常为 RuntimeException("exception")
        CompletionStage<String> future = CompletableFuture.failedStage(new RuntimeException("exception"));

        // 转换为 CompletableFuture 并验证 future 已完成
        assertTrue(future.toCompletableFuture().isDone());
        // 验证 future 是以异常形式完成的
        assertTrue(future.toCompletableFuture().isCompletedExceptionally());
    }
}


