package com.example.concurrency.completable_future.create_futures;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 使用默认构造方法创建一个未完成的任务实例，适合需要在稍后手动设置结果或异常的场景。
 * 常用于自定义异步操作的管理和控制。
 */
public class CompletableFutureConstructor {

    @Test
    public void testConstructor() throws InterruptedException, ExecutionException {
        // 创建一个未完成的 CompletableFuture 实例
        CompletableFuture<String> future = new CompletableFuture<>();

        // 验证 future 的初始状态为未完成
        assertFalse(future.isDone());

        // 手动完成 future，将其结果设置为 "value"
        future.complete("value");

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 的结果是否为 "value"
        assertEquals("value", future.get());
    }
}

