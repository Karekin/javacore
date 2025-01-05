package com.example.concurrency.completable_future.part0;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 适用场景：
 * 此代码演示了如何通过 CompletableFuture 的流水线方法构建异步计算流程。
 * 适用于多步骤异步计算的场景，例如科学计算、数据处理等。
 *
 * 注意事项：
 * 异步任务之间的依赖关系需明确，确保结果按预期流转。
 * 在生产环境中，应尽量避免使用 join 阻塞线程，推荐使用非阻塞的方式处理结果。
 */
public class MethodsToPipelineComputations extends Demo {

    // 计算圆的面积公式：area = π * r^2
    @Test
    public void test() {
        // 创建一个 CompletableFuture，用于异步提供圆周率 π 的值
        CompletableFuture<Double> pi = CompletableFuture.supplyAsync(() -> Math.PI);

        // 创建一个 CompletableFuture，用于异步提供半径 r 的值
        CompletableFuture<Integer> radius = CompletableFuture.supplyAsync(() -> 1);

        // 创建一个 CompletableFuture，通过流水线的方式计算圆的面积
        CompletableFuture<Void> area = radius
                // 第一步：计算 r^2，即半径的平方
                .thenApply(r -> r * r)
                // 第二步：与 π 相乘，得到圆的面积
                .thenCombine(pi, (multiplier1, multiplier2) -> multiplier1 * multiplier2)
                // 第三步：消费计算结果（圆的面积），输出日志
                .thenAccept(a -> logger.info("area: {}", a))
                // 第四步：在所有操作完成后执行一个额外的操作，记录完成日志
                .thenRun(() -> logger.info("operation completed"));

        // 阻塞当前线程，等待所有任务完成
        area.join();
    }
}
