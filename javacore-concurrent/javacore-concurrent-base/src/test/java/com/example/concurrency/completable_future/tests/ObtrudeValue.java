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
 * 强制设置任务的结果，无论任务的当前状态如何（未完成、正常完成或异常完成）。
 * 适用于调试、模拟等特殊场景。
 */
public class ObtrudeValue extends Demo {

    @Test
    public void testObtrudeValueOnCompletedFuture() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，初始结果为 "value1"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value1");

        // 验证初始状态和结果
        assertTrue(future.isDone()); // 已完成
        assertFalse(future.isCompletedExceptionally()); // 未以异常完成
        assertEquals("value1", future.get()); // 初始结果为 "value1"

        // 使用 obtrudeValue 强制更改结果为 "value2"
        future.obtrudeValue("value2");

        // 验证状态和结果是否更新
        assertTrue(future.isDone()); // 仍然已完成
        assertFalse(future.isCompletedExceptionally()); // 仍未以异常完成
        assertEquals("value2", future.get()); // 更新后的结果为 "value2"
    }

    @Test
    public void testObtrudeValueOnFailedFuture() throws InterruptedException, ExecutionException {
        // 创建一个以异常完成的 CompletableFuture，异常信息为 "error"
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException("error"));

        // 验证初始状态
        assertTrue(future.isDone()); // 已完成
        assertTrue(future.isCompletedExceptionally()); // 以异常完成

        // 使用 obtrudeValue 强制更改结果为 "value"
        future.obtrudeValue("value");

        // 验证状态和结果是否更新
        assertTrue(future.isDone()); // 仍然已完成
        assertFalse(future.isCompletedExceptionally()); // 更新为未以异常完成
        assertEquals("value", future.get()); // 更新后的结果为 "value"
    }

    @Test
    public void testObtrudeValueOnPendingFuture() throws InterruptedException, ExecutionException {
        // 创建一个未完成的 CompletableFuture
        CompletableFuture<String> future = new CompletableFuture<>();

        // 验证初始状态
        assertFalse(future.isDone()); // 未完成

        // 使用 obtrudeValue 强制设置结果为 "value"
        future.obtrudeValue("value");

        // 验证状态和结果是否更新
        assertTrue(future.isDone()); // 更新为已完成
        assertFalse(future.isCompletedExceptionally()); // 未以异常完成
        assertEquals("value", future.get()); // 设置的结果为 "value"
    }
}

