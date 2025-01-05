package com.example.concurrency.completable_future.create_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * newIncompleteFuture 适用于基于现有的 CompletableFuture 类型创建新实例的场景，例如扩展或组合任务。
 * 手动完成任务可用于控制异步流程中的任务状态和结果。
 */
public class NewIncompleteFuture extends Demo {

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，结果值为 "value1"
        CompletableFuture<String> future1 = CompletableFuture.completedFuture("value1");
        // 验证 future1 已经完成
        assertTrue(future1.isDone());

        // 使用 newIncompleteFuture 方法创建一个新的未完成的 CompletableFuture
        // 该方法基于当前 CompletableFuture 的类型创建一个新的实例，但初始状态为未完成
        CompletableFuture<String> future2 = future1.newIncompleteFuture();
        // 验证 future2 尚未完成
        assertFalse(future2.isDone());

        // 手动完成 future2，将其结果设置为 "value2"
        future2.complete("value2");

        // 验证 future2 已完成
        assertTrue(future2.isDone());
        // 验证 future2 的结果是否为 "value2"
        assertEquals("value2", future2.get());
    }
}

