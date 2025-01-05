package com.example.concurrency.completable_future.tests;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 当需要创建一个现有 CompletableFuture 的副本，且希望副本的状态和结果与原 CompletableFuture 一致时，可以使用 copy() 方法。
 *
 * 注意事项：
 * copy() 创建的副本是一个全新的 CompletableFuture 实例，与原实例相互独立。
 * 对副本的任何操作（如 complete() 或 completeExceptionally()）不会影响原 CompletableFuture。
 */
public class Copy extends Demo {

    @Test
    public void testObtrudeException1() {
        // 创建一个已完成的 CompletableFuture，其结果为 "value"
        CompletableFuture<String> future1 = CompletableFuture.completedFuture("value");

        // 验证 future1 的状态为已完成
        assertTrue(future1.isDone());

        // 使用 copy() 方法复制 future1，创建一个新的 CompletableFuture 实例
        CompletableFuture<String> future2 = future1.copy();

        // 验证复制后的 future2 的状态与 future1 一致，为已完成状态
        assertTrue(future2.isDone());
    }
}

