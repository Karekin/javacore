package com.example.concurrency.completable_future.handle_exceptions;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * 对比：
 * 与 handle 不同，whenComplete 不会改变任务的结果，仅用于处理任务完成后的状态。
 * 适合记录日志或执行收尾操作，而不影响任务链的后续操作。
 *
 * 适用场景：
 * 任务完成后记录日志或发送通知。
 * 确保某些操作无论任务成功或失败都能执行（如清理资源）。
 */

public class WhenComplete extends Demo {

    @Test
    public void testWhenCompleteSuccess() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value")
                // 使用 whenComplete 方法，定义回调函数处理正常结果或异常
                .whenComplete((value, t) -> {
                    if (t == null) { // 如果没有异常发生
                        logger.info("success: {}", value); // 打印正常结果
                    } else { // 如果发生异常
                        logger.warn("failure: {}", t.getMessage()); // 打印异常信息
                    }
                });

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常的形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证最终结果是否为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void testWhenCompleteError() {
        // 创建一个已失败的 CompletableFuture，抛出 RuntimeException("exception")
        CompletableFuture<String> future = CompletableFuture.<String>failedFuture(new RuntimeException("exception"))
                // 使用 whenComplete 方法，定义回调函数处理正常结果或异常
                .whenComplete((value, t) -> {
                    if (t == null) { // 如果没有异常发生
                        logger.info("success: {}", value); // 打印正常结果
                    } else { // 如果发生异常
                        logger.warn("failure: {}", t.getMessage()); // 打印异常信息
                    }
                });

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 是否以异常的形式完成
        assertTrue(future.isCompletedExceptionally());
    }
}
