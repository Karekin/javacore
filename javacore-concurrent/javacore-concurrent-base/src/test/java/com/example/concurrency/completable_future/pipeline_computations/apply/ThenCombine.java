package com.example.concurrency.completable_future.pipeline_computations.apply;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;

/**
 * thenCombine 方法：
 * 当 stage1 和 stage2 都完成后，thenCombine 会将两个任务的结果传递给回调函数。
 * 回调函数接收两个结果（s1 和 s2），将它们合并为一个字符串，并将合并后的结果转换为大写。
 */
public class ThenCombine extends Demo {

    @Test
    public void testThenCombine() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟耗时操作，返回字符串 "parallel1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet("parallel1"));

        // 创建第二个异步任务（stage2），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟耗时操作，返回字符串 "parallel2"
        CompletionStage<String> stage2 = supplyAsync(() -> sleepAndGet("parallel2"));

        // 使用 thenCombine 方法，当 stage1 和 stage2 都完成后，
        // 将它们的结果合并，并执行合并逻辑，将结果拼接为一个大写字符串
        CompletionStage<String> stage = stage1.thenCombine(stage2,
                (s1, s2) -> (s1 + " " + s2).toUpperCase()); // 将 "parallel1" 和 "parallel2" 合并为大写 "PARALLEL1 PARALLEL2"

        // 等待 stage 完成，并验证返回结果是否为 "PARALLEL1 PARALLEL2"
        assertEquals("PARALLEL1 PARALLEL2", stage.toCompletableFuture().get());
    }
}

