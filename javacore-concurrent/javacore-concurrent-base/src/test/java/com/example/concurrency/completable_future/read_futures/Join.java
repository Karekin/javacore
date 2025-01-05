package com.example.concurrency.completable_future.read_futures;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

/**
 * 适用场景：
 * join() 适用于需要同步等待任务完成并获取结果的场景，尤其是在不需要处理受检异常的情况下。
 * 比 get() 更简洁，但需注意异常处理逻辑的差异。
 *
 * 注意事项：
 * 如果任务以异常形式完成，join() 会抛出 CompletionException，可以通过 CompletionException.getCause() 获取原始异常。
 * join() 会阻塞当前线程，应避免在主线程或高性能需求的场景中直接调用，建议结合非阻塞操作使用。
 */
public class Join extends Demo {

    @Test
    public void testJoin() {
        // 使用 supplyAsync 方法创建一个异步任务
        // 模拟耗时操作，2 秒后返回结果 "value"
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));

        // 使用 join 方法获取结果
        // 若任务已完成，返回结果 "value"
        // 若任务未完成，则阻塞直到任务完成
        // 与 get 方法不同，join 不会抛出受检异常
        assertEquals("value", future.join());
    }
}

