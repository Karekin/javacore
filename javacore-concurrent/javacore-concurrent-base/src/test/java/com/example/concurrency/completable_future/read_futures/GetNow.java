package com.example.concurrency.completable_future.read_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 当希望非阻塞地检查并获取任务结果时，可以使用 getNow 方法。
 * 如果任务尚未完成，则可以使用默认值作为占位。
 *
 * 注意事项：
 * 与 get() 方法不同，getNow 不会阻塞线程。
 * 适用于需要快速检查任务结果或在任务未完成时使用默认值的场景。
 * 如果任务以异常形式完成，getNow 会抛出异常。
 */
public class GetNow extends Demo {

    @Test
    public void getNow() {
        // 创建一个已完成的 CompletableFuture，结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 使用 getNow 方法直接获取结果，若任务已完成，返回任务的结果 "value"
        assertEquals("value", future.getNow("default"));
        // 验证 future 已完成
        assertTrue(future.isDone());
    }

    @Test
    public void getNowValueIfAbsent() {
        // 使用 supplyAsync 方法创建一个异步任务
        // 模拟耗时操作，最终返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

        // 使用 getNow 方法尝试直接获取结果
        // 若任务尚未完成，则返回提供的默认值 "default"
        assertEquals("default", future.getNow("default"));
        // 验证 future 尚未完成
        assertFalse(future.isDone());
    }
}

