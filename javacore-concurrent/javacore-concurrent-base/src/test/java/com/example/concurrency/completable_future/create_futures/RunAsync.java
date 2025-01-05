package com.example.concurrency.completable_future.create_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNull;

/**
 * 适用场景：
 * runAsync 适用于需要在后台执行任务但不需要返回结果的场景，例如日志记录、触发事件或清理操作。
 */
public class RunAsync extends Demo {

    @Test
    public void testRunAsync() throws InterruptedException, ExecutionException {
        // 使用 runAsync 方法创建一个异步任务
        // runAsync 用于执行无返回值的异步任务，任务逻辑由传入的 Runnable 定义
        CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                logger.info("action") // 在异步任务中打印日志 "action"
        );

        // 阻塞等待异步任务完成，并验证返回值是否为 null
        // runAsync 返回的 CompletableFuture<Void> 其 get 方法的结果始终为 null
        assertNull(future.get());
    }
}
