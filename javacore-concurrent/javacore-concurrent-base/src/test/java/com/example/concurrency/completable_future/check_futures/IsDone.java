package com.example.concurrency.completable_future.check_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 适用场景：
 * 使用这些方法可以检查任务的当前状态，便于对异步流程的监控和管理。
 *
 * 例如：
 * 判断任务是否完成以触发后续操作。
 * 判断任务是否异常完成以执行异常处理逻辑。
 * 判断任务是否被取消以记录或重试任务。
 */
public class IsDone extends Demo {

    @Test
    public void testIsDoneTrue() throws InterruptedException, ExecutionException {
        // 创建一个已完成的 CompletableFuture，结果值为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 验证 future 已完成
        assertTrue(future.isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证 future 没有被取消
        assertFalse(future.isCancelled());
        // 验证 future 的结果是否为 "value"
        assertEquals("value", future.get());
    }

    @Test
    public void testIsDoneFalse() {
        // 使用 supplyAsync 创建一个异步任务，任务的结果为 sleepAndGet("value")
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

        // 验证 future 尚未完成
        assertFalse(future.isDone());
        // 验证 future 没有以异常形式完成
        assertFalse(future.isCompletedExceptionally());
        // 验证 future 没有被取消
        assertFalse(future.isCancelled());
    }
}

