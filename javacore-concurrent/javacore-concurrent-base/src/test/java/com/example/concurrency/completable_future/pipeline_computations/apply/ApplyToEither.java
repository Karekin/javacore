package com.example.concurrency.completable_future.pipeline_computations.apply;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;

/**
 * applyToEither 方法：
 * 该方法会在 stage1 或 stage2 中最先完成的任务完成后，执行指定的回调逻辑。
 * 回调逻辑将接收到的任务结果转换为大写字符串（toUpperCase）。
 */
public class ApplyToEither extends Demo {

    @Test
    public void testApplyToEither() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），延迟 1 秒后返回字符串 "parallel1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet(1, "parallel1"));

        // 创建第二个异步任务（stage2），延迟 2 秒后返回字符串 "parallel2"
        CompletionStage<String> stage2 = supplyAsync(() -> sleepAndGet(2, "parallel2"));

        // 使用 applyToEither 方法，当 stage1 或 stage2 中任意一个任务完成时，
        // 执行提供的回调逻辑，将完成任务的结果转换为大写
        CompletionStage<String> stage = stage1.applyToEither(stage2,
                String::toUpperCase); // 将最先完成的任务结果转换为大写

        // 等待 stage 完成，并验证返回的结果是否为 "PARALLEL1"
        assertEquals("PARALLEL1", stage.toCompletableFuture().get());
    }
}

