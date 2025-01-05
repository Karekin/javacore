package com.example.concurrency.completable_future.pipeline_computations.apply;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;

/**
 * thenApply：用于直接对结果进行转换，适合简单的数据处理。
 * thenApply（嵌套）：需要显式管理嵌套的异步任务，代码复杂且难以维护。
 * thenCompose：将嵌套的异步任务平铺为同一层级，简化了代码逻辑，推荐用于复杂的异步任务链。
 */
public class ThenApply_vs_ThenCompose extends Demo {

    // 测试 thenApply 方法用于简单的数据转换
    @Test
    public void testThenApplyFast() throws Exception {
        // 创建一个异步任务，初始值为 2
        CompletableFuture<Integer> future = supplyAsync(() -> 2)
                // 使用 thenApply 方法，将结果增加 3，返回 5
                .thenApply(i -> i + 3); // Function<Integer, Integer>

        // 验证最终结果是否为 5
        assertEquals(5, future.get().intValue());
    }

    // 测试 thenApply 方法用于嵌套异步任务的处理
    @Test
    public void testThenApplySlow() throws Exception {
        // 创建一个异步任务，初始值为 2
        CompletableFuture<CompletableFuture<Integer>> future1 = supplyAsync(() -> 2)
                // 使用 thenApply 方法，将结果转化为一个新的异步任务
                .thenApply(i -> supplyAsync(() -> i + 3)); // Function<Integer, CompletableFuture<Integer>>

        // 获取 thenApply 生成的 CompletableFuture（嵌套异步任务），这里会阻塞等待 future1 完成
        CompletableFuture<Integer> future2 = future1.get(); // blocking

        // 验证最终结果是否为 5
        assertEquals(5, future2.get().intValue());
    }

    // 测试 thenCompose 方法用于处理嵌套的异步任务
    @Test
    public void testThenCompose() throws Exception {
        // 创建一个异步任务，初始值为 2
        CompletableFuture<Integer> future = supplyAsync(() -> 2)
                // 使用 thenCompose 方法，将结果转化为一个新的异步任务，并将其展开为同一层级
                .thenCompose(i -> supplyAsync(() -> i + 3)); // Function<Integer, CompletionStage<Integer>>

        // 验证最终结果是否为 5
        assertEquals(5, future.get().intValue());
    }
}

