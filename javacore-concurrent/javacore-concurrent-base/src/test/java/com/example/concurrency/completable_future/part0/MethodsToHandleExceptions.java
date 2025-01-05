package com.example.concurrency.completable_future.part0;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 适用场景：
 * 异步任务链中涉及复杂的异常处理和恢复逻辑时。
 * 使用 handle 提供默认值或恢复策略，确保任务链不中断。
 *
 * 注意事项：
 * 未恢复的异常会导致后续任务被跳过。
 * 使用 handle 可以捕获异常并提供替代结果，从而恢复任务链的执行。
 */
public class MethodsToHandleExceptions extends Demo {

    @Test
    public void test() {
        CompletableFuture.supplyAsync(() -> 0) // 异步任务，初始值为 0
                .thenApply(i -> {
                    // 阶段 1：尝试计算 1 / i，此处 i 为 0，会抛出 ArithmeticException
                    logger.info("stage 1: {}", i);
                    return 1 / i; // 由于除以零，抛出异常，导致后续任务跳过
                })
                .thenApply(i -> {
                    // 阶段 2：此阶段被跳过，因为前一阶段抛出了异常
                    logger.info("stage 2: {}", i);
                    return 1 / i;
                })
                .whenComplete((value, t) -> {
                    // 异常处理：捕获异常并记录日志
                    if (t == null) {
                        // 如果没有异常，记录成功结果
                        logger.info("success: {}", value);
                    } else {
                        // 如果有异常，记录异常信息
                        logger.warn("failure: {}", t.getMessage());
                    }
                })
                .thenApply(i -> {
                    // 阶段 3：此阶段被跳过，因为前一阶段未成功恢复
                    logger.info("stage 3: {}", i);
                    return 1 / i;
                })
                .handle((value, t) -> {
                    // 异常恢复：捕获异常并提供恢复机制
                    if (t == null) {
                        // 如果没有异常，返回正常结果 + 1
                        return value + 1;
                    } else {
                        // 如果有异常，返回恢复值 -1
                        return -1;
                    }
                })
                .thenApply(i -> {
                    // 阶段 4：异常已恢复，继续执行后续任务
                    logger.info("stage 4: {}", i);
                    return 1 / i; // 此处 i 为 -1，因此计算结果为 -1
                })
                .join(); // 阻塞当前线程，等待所有任务完成
    }
}

