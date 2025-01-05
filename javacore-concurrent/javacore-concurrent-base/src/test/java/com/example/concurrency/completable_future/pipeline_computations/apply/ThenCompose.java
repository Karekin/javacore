package com.example.concurrency.completable_future.pipeline_computations.apply;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;

/**
 * thenCompose 方法：
 * 在 stage1 完成后，thenCompose 基于其结果 s 创建并返回一个新的异步任务。
 * 新任务通过 supplyAsync 执行，将 stage1 的结果与 "sequential2" 拼接，并将拼接后的结果转换为大写。
 */
public class ThenCompose extends Demo {

    @Test
    public void testThenCompose() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟耗时操作，返回字符串 "sequential1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet("sequential1"));

        // 使用 thenCompose 方法，在 stage1 完成后，基于其结果创建并返回一个新的异步任务
        CompletionStage<String> stage = stage1.thenCompose(
                s -> supplyAsync(() ->
                        // 创建新的异步任务，将 stage1 的结果与 "sequential2" 拼接并转换为大写
                        sleepAndGet((s + " " + "sequential2").toUpperCase())
                )
        );

        // 等待 stage 完成，并验证最终返回结果是否为 "SEQUENTIAL1 SEQUENTIAL2"
        assertEquals("SEQUENTIAL1 SEQUENTIAL2", stage.toCompletableFuture().get());
    }
}
