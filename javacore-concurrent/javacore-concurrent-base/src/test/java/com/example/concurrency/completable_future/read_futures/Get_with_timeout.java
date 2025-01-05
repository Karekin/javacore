package com.example.concurrency.completable_future.read_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 适用场景：
 * 当需要限制任务的最大等待时间时，可以使用 get(long timeout, TimeUnit unit) 方法。
 * 适用于需要防止任务长时间阻塞的场景，例如高性能系统或响应时间敏感的任务。
 *
 * 注意事项：
 * 如果任务在超时之前完成，get 方法返回正常结果。
 * 如果任务超时，get 方法会抛出 TimeoutException，但任务仍会继续运行，直到自然完成或被取消。
 * 建议结合超时处理逻辑，确保系统性能和用户体验。
 */
public class Get_with_timeout extends Demo {

    @Test
    public void testGetWithTimeoutSuccess() throws InterruptedException, ExecutionException, TimeoutException {
        // 使用 supplyAsync 方法创建一个异步任务
        // 模拟耗时操作，2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));

        // 使用 get(long timeout, TimeUnit unit) 方法获取结果
        // 设置超时时间为 3 秒，任务在超时之前完成，返回结果 "value"
        assertEquals("value", future.get(3, TimeUnit.SECONDS));
    }

    @Test
    public void testGetWithTimeoutFailure() throws InterruptedException, ExecutionException {
        // 使用 supplyAsync 方法创建一个异步任务
        // 模拟耗时操作，2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));

        try {
            // 使用 get(long timeout, TimeUnit unit) 方法获取结果
            // 设置超时时间为 1 秒，任务未能在超时之前完成，抛出 TimeoutException
            future.get(1, TimeUnit.SECONDS);
            // 如果没有抛出异常，则测试失败
            fail();
        } catch (TimeoutException e) {
            // 捕获 TimeoutException，验证超时行为符合预期
            assertTrue(true);
        }
    }
}
