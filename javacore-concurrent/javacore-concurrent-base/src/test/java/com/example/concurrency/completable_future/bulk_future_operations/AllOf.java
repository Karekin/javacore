package com.example.concurrency.completable_future.bulk_future_operations;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;

/**
 * 适用场景：
 *
 * allOf：
 * 适用于需要等待多个异步任务完成的场景。
 * 如批量 API 请求、文件处理等。
 *
 * runAfterBoth：
 * 适用于需要在两个任务完成后执行额外操作的场景。
 * 如数据合并或状态更新。
 *
 * 注意事项：
 * join 方法用于获取任务结果，会抛出运行时异常，需要确保任务正常完成。
 * 使用 allOf 时，任务本身的异常不会被传播到返回的 CompletableFuture<Void>，需要单独检查每个任务的状态。
 */
public class AllOf extends Demo {

    @Test
    public void testAllOf() throws InterruptedException, ExecutionException {
        // 创建一个 CompletableFuture 数组，模拟多个并行任务
        CompletableFuture<?>[] futures = new CompletableFuture<?>[]{
                supplyAsync(() -> sleepAndGet(1, "parallel1")), // 模拟耗时 1 秒的任务
                supplyAsync(() -> sleepAndGet(2, "parallel2")), // 模拟耗时 2 秒的任务
                supplyAsync(() -> sleepAndGet(3, "parallel3"))  // 模拟耗时 3 秒的任务
        };

        // 使用 CompletableFuture.allOf 聚合所有 CompletableFuture
        // 返回一个新的 CompletableFuture<Void>，当所有任务完成时，该 CompletableFuture 被标记为完成
        CompletableFuture<Void> future = CompletableFuture.allOf(futures);

        // 阻塞当前线程，等待所有任务完成
        future.get();

        // 将所有任务的结果组合成一个字符串
        String result = Stream.of(futures) // 将数组转换为流
                .map(CompletableFuture::join) // 获取每个 CompletableFuture 的结果
                .map(Object::toString) // 将结果转换为字符串
                .collect(Collectors.joining(" ")); // 用空格拼接结果

        // 验证拼接结果是否符合预期
        assertEquals("parallel1 parallel2 parallel3", result);
    }

    @Test
    public void testRunAfterBoth() throws InterruptedException, ExecutionException {
        // 创建两个并行任务
        CompletableFuture<String> future1 = supplyAsync(() -> sleepAndGet(1, "parallel1")); // 模拟耗时 1 秒
        CompletableFuture<String> future2 = supplyAsync(() -> sleepAndGet(2, "parallel2")); // 模拟耗时 2 秒

        // 使用 runAfterBoth 在两个任务完成后执行一个操作
        // 返回一个新的 CompletableFuture<Void>，表示两个任务完成后的操作
        CompletableFuture<Void> future = future1
                .runAfterBoth(future2, () -> {}); // 此处的操作为空

        // 阻塞当前线程，等待两个任务都完成
        future.get();

        // 将两个任务的结果组合成一个字符串
        String result = Stream.of(future1, future2) // 将两个 CompletableFuture 转换为流
                .map(CompletableFuture::join) // 获取每个 CompletableFuture 的结果
                .map(Object::toString) // 将结果转换为字符串
                .collect(Collectors.joining(" ")); // 用空格拼接结果

        // 验证拼接结果是否符合预期
        assertEquals("parallel1 parallel2", result);
    }
}

