package com.example.concurrency.completable_future.pipeline_computations.accept;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertNull;

/**
 * acceptEither 方法：
 * 用于监听两个任务中最先完成的任务，并消费其结果。
 * 不等待所有任务完成，而是以最快完成的任务为主。
 */
public class AcceptEither extends Demo {

    @Test
    public void testAcceptEither() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），执行 sleepAndGet 方法，延迟 1 秒返回 "parallel1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet(1, "parallel1"));

        // 创建第二个异步任务（stage2），执行 sleepAndGet 方法，延迟 2 秒返回 "parallel2"
        CompletionStage<String> stage2 = supplyAsync(() -> sleepAndGet(2, "parallel2"));

        // 使用 acceptEither 方法，选择最先完成的任务，并消费其结果
        // 当 stage1 或 stage2 中任何一个任务最先完成时，执行 lambda 表达式
        CompletionStage<Void> stage = stage1.acceptEither(stage2,
                s -> logger.info("consumes the first: {}", s)); // 打印最先完成的任务结果

        // 等待 stage 完成，并验证返回值为 null（acceptEither 的返回类型为 CompletionStage<Void>）
        assertNull(stage.toCompletableFuture().get());
    }
}

