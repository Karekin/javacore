package com.example.concurrency.completable_future.complete_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 当需要异步完成一个任务时，可以使用 completeAsync 方法，例如在后台线程中加载数据或执行计算。
 *
 * 行为特点：
 * completeAsync 异步执行完成任务的逻辑，避免阻塞主线程。
 * 提供更灵活的完成方式，适合需要非阻塞操作的场景。
 *
 * 注意事项：
 * 如果 Supplier 函数中的逻辑较为耗时，可以使用自定义线程池以避免占用默认的公共线程池。
 */
public class CompleteAsync extends Demo {

    @Test
    public void testCompleteAsync() throws InterruptedException, ExecutionException {
        // 创建一个未完成的 CompletableFuture 实例
        CompletableFuture<String> future1 = new CompletableFuture<>();

        // 验证 future1 尚未完成
        assertFalse(future1.isDone());

        // 调用 completeAsync 方法，异步完成 future1，并设置结果为 "value"
        // completeAsync 方法会在默认的 ForkJoinPool 线程池中异步执行提供的 Supplier 函数
        CompletableFuture<String> future2 = future1.completeAsync(() -> "value");

        // 模拟等待一段时间，确保异步操作完成
        sleep(1);

        // 验证 future2 已完成
        assertTrue(future2.isDone());
        // 验证 future2 的结果是否为 "value"
        assertEquals("value", future2.get());
    }
}

