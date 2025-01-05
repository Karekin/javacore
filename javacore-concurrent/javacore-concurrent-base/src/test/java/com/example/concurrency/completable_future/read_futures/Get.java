package com.example.concurrency.completable_future.read_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * 适用场景：
 * 当需要获取异步任务的最终结果时，使用 get() 方法可以确保任务完成后返回结果。
 * 常用于单元测试或需要同步获取结果的场景。
 *
 * 注意事项：
 * get() 方法会阻塞当前线程，直到任务完成，因此可能导致性能下降或线程被阻塞过长时间。
 * 在实际开发中，应尽量避免在主线程中使用 get() 方法，建议使用非阻塞的方式（如 thenApply 或 whenComplete）处理异步结果。
 */
public class Get extends Demo {

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        // 使用 supplyAsync 方法创建一个异步任务
        // 模拟耗时操作，2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));

        // 使用 get() 方法阻塞当前线程，直到异步任务完成并返回结果
        // 验证异步任务的结果是否为 "value"
        assertEquals("value", future.get());
    }
}
