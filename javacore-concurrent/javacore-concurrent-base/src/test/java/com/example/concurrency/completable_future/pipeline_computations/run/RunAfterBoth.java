package com.example.concurrency.completable_future.pipeline_computations.run;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertNull;

/**
 * runAfterBoth 方法：
 * 当 stage1 和 stage2 都完成后，runAfterBoth 会触发指定的回调操作。
 * 回调操作不依赖任务的结果，仅执行指定的逻辑。
 */
public class RunAfterBoth extends Demo {

    @Test
    public void testRunAfterBoth() throws InterruptedException, ExecutionException {
        // 创建第一个异步任务（stage1），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟延迟 1 秒后返回字符串 "parallel1"
        CompletionStage<String> stage1 = supplyAsync(() -> sleepAndGet(1, "parallel1"));

        // 创建第二个异步任务（stage2），通过 supplyAsync 方法调用 sleepAndGet，
        // 模拟延迟 2 秒后返回字符串 "parallel2"
        CompletionStage<String> stage2 = supplyAsync(() -> sleepAndGet(2, "parallel2"));

        // 使用 runAfterBoth 方法，当 stage1 和 stage2 都完成后，
        // 执行指定的回调操作（不依赖任务结果）
        CompletionStage<Void> stage = stage1.runAfterBoth(stage2,
                () -> logger.info("runs after both")); // 打印日志，表示两个任务都完成后触发了回调

        // 等待 stage 完成，并验证返回值为 null（runAfterBoth 的返回类型为 CompletionStage<Void>）
        assertNull(stage.toCompletableFuture().get());
    }
}
