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
 * 当需要手动设置异步任务的结果时，使用 complete 方法可以快速完成任务。
 * 适合处理某些需要外部条件完成的任务，例如用户输入、事件触发或资源加载完成。
 *
 * 行为特点：
 * 如果任务已完成，complete 不会更改任务状态或结果，确保任务的幂等性。
 * 与自动完成（异步操作完成）相比，complete 提供了手动干预的能力。
 */
public class Complete extends Demo {

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        // 创建一个未完成的 CompletableFuture 实例
        CompletableFuture<String> future = new CompletableFuture<>();

        // 验证 future 尚未完成
        assertFalse(future.isDone());

        // 调用 complete 方法，将 CompletableFuture 的结果设置为 "value"
        // 如果 future 尚未完成，complete 方法返回 true；如果已经完成，返回 false
        boolean hasCompleted = future.complete("value");

        // 验证 complete 方法返回 true，表示设置结果成功
        assertTrue(hasCompleted);
        // 验证 future 状态为已完成
        assertTrue(future.isDone());
        // 验证 future 的结果是否为 "value"
        assertEquals("value", future.get());
    }
}

