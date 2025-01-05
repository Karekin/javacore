package com.example.concurrency.completable_future.pipeline_computations.apply;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;

/**
 * thenApply 方法：
 * thenApply 用于在 stage1 完成后，对其结果进行同步转换（不涉及新线程）。
 * 这里的转换逻辑是将结果字符串转换为大写字符串。
 */
public class ThenApply extends Demo {

    @Test
    public void testThenApply() throws InterruptedException, ExecutionException {
        // 创建一个异步任务（stage1），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟耗时操作，返回字符串 "single"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet("single"));

        // 使用 thenApply 方法，在 stage1 完成后对其结果进行转换
        // 回调函数将结果字符串转换为大写
        CompletionStage<String> stage = stage1.thenApply(
                String::toUpperCase); // 将结果 "single" 转换为 "SINGLE"

        // 等待 stage 完成，并验证返回结果是否为 "SINGLE"
        assertEquals("SINGLE", stage.toCompletableFuture().get());
    }
}

