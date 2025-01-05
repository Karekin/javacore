package com.example.concurrency.completable_future.complete_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * 适用场景：
 * cancel() 方法适用于需要主动终止任务的场景，例如超时处理或用户主动取消操作。
 * 检查 isCancelled() 可以在任务被取消后执行补偿逻辑或记录状态。
 */
public class Cancel extends Demo {

    @Test
    public void testCancel() throws InterruptedException, ExecutionException {
        // 创建一个未完成的 CompletableFuture 实例
        CompletableFuture<String> future = new CompletableFuture<>();

        // 验证 future 尚未完成
        assertFalse(future.isDone());
        // 验证 future 尚未被取消
        assertFalse(future.isCancelled());

        // 调用 cancel(false) 方法尝试取消任务
        // 参数 false 表示即使任务正在运行，也不会尝试中断任务线程
        boolean isCanceled = future.cancel(false);

        // 验证 cancel 方法返回 true，表示取消成功
        assertTrue(isCanceled);
        // 验证 future 状态为已完成（取消视为完成的一种形式）
        assertTrue(future.isDone());
        // 验证 future 已被取消
        assertTrue(future.isCancelled());

        try {
            // 调用 get() 方法尝试获取结果，由于任务被取消，会抛出 CancellationException
            future.get();
            // 如果没有抛出异常，则测试失败
            fail();
        } catch (CancellationException e) {
            // 捕获 CancellationException，验证抛出的异常符合预期
            assertTrue(true);
        }
    }
}

