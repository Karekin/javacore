package com.example.concurrency.completable_future.create_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 当需要直接返回一个已完成的任务时，可使用这两个方法快速创建对应实例，例如用于单元测试或任务链的起点。
 */
public class CompletedFuture extends Demo {

    @Test
    public void testCompletedFuture() throws InterruptedException, ExecutionException {
        // 使用 CompletableFuture.completedFuture 创建一个已完成的 CompletableFuture
        // 结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证 future 的结果是否为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void testCompletedStage() throws InterruptedException, ExecutionException {
        // 使用 CompletableFuture.completedStage 创建一个已完成的 CompletionStage
        // 结果值为 "value"
        CompletionStage<String> future = CompletableFuture.completedStage("value");

        // 转换为 CompletableFuture 并验证已完成状态
        assertTrue(future.toCompletableFuture().isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.toCompletableFuture().isCompletedExceptionally());
        // 验证 future 的结果是否为 "value"
        assertEquals("value", future.toCompletableFuture().get());
    }
}


