package com.example.concurrency.completable_future.pipeline_computations.accept;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertNull;

/**
 * thenAccept 方法：
 * 在 stage1 完成后，thenAccept 消费任务结果，但不返回新的结果。
 * 使用 logger.info 打印 stage1 的返回值，便于观察任务的执行过程。
 */
public class ThenAccept extends Demo {

    @Test
    public void testThenAccept() throws InterruptedException, ExecutionException {
        // 创建一个异步任务（stage1），调用 supplyAsync 方法执行 sleepAndGet，并返回 "single"
        CompletableFuture<String> stage1 = supplyAsync(() -> sleepAndGet("single"));

        // 使用 thenAccept 方法，在 stage1 完成后消费其结果
        // 当 stage1 完成时，执行 lambda 表达式，将结果 "single" 打印到日志中
        CompletionStage<Void> stage = stage1.thenAccept(
                s -> logger.info("consumes the single: {}", s)); // 消费 stage1 的结果

        // 等待 stage 完成，并验证返回值为 null（thenAccept 的返回类型为 CompletionStage<Void>）
        assertNull(stage.toCompletableFuture().get());
    }
}

