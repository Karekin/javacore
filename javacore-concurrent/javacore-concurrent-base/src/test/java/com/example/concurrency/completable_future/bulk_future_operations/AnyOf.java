package com.example.concurrency.completable_future.bulk_future_operations;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * 适用场景：
 *
 * anyOf：
 * 适用于需要等待多个异步任务中的任意一个完成的场景，例如并行处理请求，选择最快的结果。
 *
 * applyToEither：
 * 适用于需要在两个任务中选择第一个完成的任务，并对其结果应用某种逻辑的场景。
 *
 *
 * 注意事项：
 *
 * anyOf 的返回值类型：
 * 结果类型是 Object，需要显式转换为具体类型（如有需要）。
 *
 * applyToEither 的函数逻辑：
 * 函数逻辑会被应用到第一个完成的任务结果中，因此要确保函数逻辑适配结果类型。
 *
 * 异常处理：
 * 如果两个任务都以异常完成，applyToEither 和 anyOf 会抛出异常。
 */
public class AnyOf extends Demo {

    @Test
    public void testAnyOf() throws InterruptedException, ExecutionException {
        // 使用 CompletableFuture.anyOf 将多个异步任务聚合为一个 CompletableFuture
        // anyOf 返回的 CompletableFuture 在第一个任务完成时完成
        CompletableFuture<Object> future = CompletableFuture.anyOf(
                supplyAsync(() -> sleepAndGet(1, "parallel1")), // 模拟耗时 1 秒，返回 "parallel1"
                supplyAsync(() -> sleepAndGet(2, "parallel2")), // 模拟耗时 2 秒，返回 "parallel2"
                supplyAsync(() -> sleepAndGet(3, "parallel3"))  // 模拟耗时 3 秒，返回 "parallel3"
        );

        // 验证第一个完成的任务的结果为 "parallel1"
        assertEquals("parallel1", future.get());
    }

    @Test
    public void testApplyToEither() throws InterruptedException, ExecutionException {
        // 创建两个异步任务
        CompletableFuture<String> future1 = supplyAsync(() -> sleepAndGet(1, "parallel1")); // 耗时 1 秒，返回 "parallel1"
        CompletableFuture<String> future2 = supplyAsync(() -> sleepAndGet(2, "parallel2")); // 耗时 2 秒，返回 "parallel2"

        // 使用 applyToEither，获取第一个完成的任务结果并应用函数逻辑
        // 此处函数逻辑为直接返回完成的任务结果
        CompletableFuture<String> future = future1
                .applyToEither(future2, value -> value);

        // 验证第一个完成的任务的结果为 "parallel1"
        assertEquals("parallel1", future.get());
    }
}

