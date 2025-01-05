package com.example.concurrency.completable_future.handle_exceptions;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * 异常处理特点：
 * handle 方法会捕获所有异常，避免任务进入 CompletedExceptionally 状态。
 * 无论任务成功还是失败，都能在回调中进行统一处理。
 *
 * 适用场景：
 * 适用于需要对正常结果和异常统一处理的场景，例如记录日志、转换结果或提供默认值。
 */
public class Handle extends Demo {

    @Test
    public void testHandleSuccess() throws InterruptedException, ExecutionException {
        // 创建一个已经完成的 CompletableFuture，结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value")
                // 使用 handle 方法，处理正常完成的结果或异常
                .handle((value, t) -> {
                    if (t == null) { // 如果没有异常发生
                        return value.toUpperCase(); // 将结果转换为大写
                    } else { // 如果发生异常
                        return t.getMessage(); // 返回异常的消息
                    }
                });

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常的形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证最终的结果是否为 "VALUE"
        assertEquals("VALUE", future.get());
    }

    @Test
    public void testHandleError() throws InterruptedException, ExecutionException {
        // 创建一个已经失败的 CompletableFuture，抛出 RuntimeException("exception")
        CompletableFuture<String> future = CompletableFuture.<String>failedFuture(new RuntimeException("exception"))
                // 使用 handle 方法，处理正常完成的结果或异常
                .handle((value, t) -> {
                    if (t == null) { // 如果没有异常发生
                        return value.toUpperCase(); // 将结果转换为大写
                    } else { // 如果发生异常
                        return "failure: " + t.getMessage(); // 返回拼接的错误信息
                    }
                });

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常的形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证最终的结果是否为 "failure: exception"
        assertEquals("failure: exception", future.get());
    }
}

