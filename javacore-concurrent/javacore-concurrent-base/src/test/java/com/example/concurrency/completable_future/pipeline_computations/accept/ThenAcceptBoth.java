package com.example.concurrency.completable_future.pipeline_computations.accept;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertNull;

/**
 * thenAcceptBoth 方法：
 * 当 stage1 和 stage2 都完成时，thenAcceptBoth 会同时获取两个任务的结果，并执行回调逻辑。
 * 在回调中通过 logger.info 打印两个任务的结果 s1 和 s2。
 */
public class ThenAcceptBoth extends Demo {

    @Test
    public void testThenAcceptBoth() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），延迟 1 秒后返回结果 "parallel1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet(1, "parallel1"));

        // 创建第二个异步任务（stage2），延迟 2 秒后返回结果 "parallel2"
        CompletionStage<String> stage2 = supplyAsync(() -> sleepAndGet(2, "parallel2"));

        // 使用 thenAcceptBoth 方法，当 stage1 和 stage2 都完成后，消费它们的结果
        CompletionStage<Void> stage = stage1.thenAcceptBoth(stage2,
                (s1, s2) -> logger.info("consumes both: {} {}", s1, s2)); // 同时消费两个任务的结果

        // 等待 stage 完成，并验证返回值为 null（thenAcceptBoth 返回 CompletionStage<Void>）
        assertNull(stage.toCompletableFuture().get());
    }
}
