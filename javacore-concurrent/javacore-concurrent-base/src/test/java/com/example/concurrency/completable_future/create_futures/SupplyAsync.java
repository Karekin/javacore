package com.example.concurrency.completable_future.create_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * 适用场景：
 * supplyAsync 适用于需要在后台执行耗时任务且需要返回结果的场景，例如计算、网络请求或文件操作
 */
public class SupplyAsync extends Demo {

    @Test
    public void testSupplyAsync() throws InterruptedException, ExecutionException {
        // 使用 supplyAsync 方法创建一个异步任务
        // supplyAsync 会在默认的 ForkJoinPool 线程池中运行，返回一个 CompletableFuture
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

        // 阻塞等待异步任务完成，并验证结果是否为 "value"
        assertEquals("value", future.get());
    }
}

