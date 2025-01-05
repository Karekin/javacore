package com.example.concurrency.completable_future.tests;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

/**
 * 适用场景：
 * 当需要检查当前 CompletableFuture 是否有后续依赖任务时，可以使用此方法。
 * 适用于调试或性能优化场景，了解异步任务的依赖关系。
 *
 * 注意事项：
 * 如果在 CompletableFuture 上添加后续操作（如 thenApply、thenAccept），getNumberOfDependents() 的返回值会相应增加。
 * 此方法主要用于监控任务依赖关系，不会影响任务的执行逻辑。
 */
public class GetNumberOfDependents extends Demo {

    @Test
    public void testGetNumberOfDependents() {
        // 创建一个已完成的 CompletableFuture，其结果为 "value"
        CompletableFuture<String> future = CompletableFuture.completedFuture("value");

        // 验证 future 当前没有任何依赖于它的任务
        // getNumberOfDependents() 方法返回依赖于当前 CompletableFuture 的任务数量
        assertEquals(0, future.getNumberOfDependents());
    }
}
