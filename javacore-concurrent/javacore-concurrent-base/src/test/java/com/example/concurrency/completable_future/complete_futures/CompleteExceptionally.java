package com.example.concurrency.completable_future.complete_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 适用场景：
 * 当任务发生错误时，可以使用 completeExceptionally 方法以异常形式完成任务。
 * 常用于在异步任务链中传递异常或触发异常处理逻辑。
 *
 * 行为特点：
 * 如果任务已完成，调用 completeExceptionally 不会更改任务状态或结果，确保任务的幂等性。
 * 以异常形式完成的任务，后续的结果处理需通过异常处理逻辑处理。
 */
public class CompleteExceptionally extends Demo {

    @Test
    public void testCompleteExceptionally() throws InterruptedException {
        // 创建一个未完成的 CompletableFuture 实例
        CompletableFuture<String> future = new CompletableFuture<>();

        // 验证 future 尚未完成
        assertFalse(future.isDone());
        // 验证 future 尚未以异常形式完成
        assertFalse(future.isCompletedExceptionally());

        // 调用 completeExceptionally 方法，将 CompletableFuture 以异常形式完成
        // 设置的异常为 RuntimeException("exception")
        boolean hasCompleted = future.completeExceptionally(new RuntimeException("exception"));

        // 验证 completeExceptionally 方法返回 true，表示完成成功
        assertTrue(hasCompleted);
        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 是以异常形式完成的
        assertTrue(future.isCompletedExceptionally());

        try {
            // 调用 get() 方法尝试获取结果，由于任务以异常形式完成，会抛出 ExecutionException
            future.get();
            // 如果没有抛出异常，则测试失败
            fail();
        } catch (ExecutionException e) {
            // 捕获 ExecutionException，验证其根本原因是否为设置的异常
            Throwable cause = e.getCause();
            assertEquals(RuntimeException.class, cause.getClass()); // 验证异常类型
            assertEquals("exception", cause.getMessage()); // 验证异常消息
        }
    }
}

