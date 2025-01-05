package com.example.concurrency.completable_future.pipeline_computations.run;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertNull;

/**
 * thenRun 方法：
 * thenRun 在 stage1 完成后执行一个回调操作。
 * 回调操作不依赖 stage1 的结果，只是执行一些额外的任务，例如打印日志。
 */
public class ThenRun extends Demo {

    @Test
    public void testThenRun() throws InterruptedException, ExecutionException {
        // 创建一个异步任务（stage1），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟耗时操作，返回字符串 "single"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet("single"));

        // 使用 thenRun 方法，在 stage1 完成后执行一个不依赖结果的回调操作
        CompletionStage<Void> stage = stage1.thenRun(
                () -> logger.info("runs after the single")); // 打印日志，表示 stage1 完成后的操作

        // 等待 stage 完成，并验证返回值为 null（thenRun 的返回类型为 CompletionStage<Void>）
        assertNull(stage.toCompletableFuture().get());
    }
}

