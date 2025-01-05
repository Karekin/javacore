package com.example.concurrency.completable_future.tests;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 适用场景：
 * 使用 defaultExecutor() 方法，可以检查当前环境中 CompletableFuture 的默认执行器。
 * 在性能调优或多线程任务分配时，可以根据默认执行器的类型和性能表现调整执行器配置。
 *
 * 注意事项：
 * 默认执行器是共享的全局线程池（ForkJoinPool.commonPool），适合轻量级任务。
 * 如果任务较重或需要隔离线程池，可以通过自定义执行器替代默认执行器。
 */
public class DefaultExecutor extends Demo {

    @Test
    public void testDefaultExecutor() {
        // 创建一个已完成的 CompletableFuture，其结果为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 调用 defaultExecutor() 方法获取 CompletableFuture 的默认执行器
        // 默认执行器通常是 ForkJoinPool.commonPool，负责执行异步任务
        System.out.println(future.defaultExecutor());
    }
}

