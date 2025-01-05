package com.example.concurrency.completable_future.pipeline_computations.run;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertNull;

/**
 * runAfterEither 方法：
 * runAfterEither 会在 stage1 或 stage2 中任意一个任务最先完成时触发回调操作。
 * 回调操作不依赖任务结果，只执行指定的逻辑（例如打印日志）。
 */
public class RunAfterEither extends Demo {

    @Test
    public void testRunAfterEither() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟延迟 1 秒后返回字符串 "parallel1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet(1, "parallel1"));

        // 创建第二个异步任务（stage2），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟延迟 2 秒后返回字符串 "parallel2"
        CompletionStage<String> stage2 = supplyAsync(() -> sleepAndGet(2, "parallel2"));

        // 使用 runAfterEither 方法，当 stage1 或 stage2 中任意一个任务完成时，
        // 触发回调操作（不依赖任务结果）
        CompletionStage<Void> stage = stage1.runAfterEither(stage2,
                () -> logger.info("runs after the first")); // 打印日志，表示第一个完成的任务触发了回调

        // 等待 stage 完成，并验证返回值为 null（runAfterEither 的返回类型为 CompletionStage<Void>）
        assertNull(stage.toCompletableFuture().get());
    }
}

