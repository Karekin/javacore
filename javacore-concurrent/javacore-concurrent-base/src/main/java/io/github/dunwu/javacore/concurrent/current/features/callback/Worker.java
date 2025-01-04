package io.github.dunwu.javacore.concurrent.current.features.callback;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;

/**
 * @author yangzijing
 */
@Slf4j
public class Worker {

    // 同步工作方法
    public void workSync(String someWork) {
        String result = "(" + someWork + ")";
        log.info("完成工作（同步）：{}", result);
    }

    // 异步工作方法（带回调）
    public void workAsync(Callback<String> callback, String someWork) {
        CompletableFuture.runAsync(() -> {
            String result = "(" + someWork + ")";
            log.info("完成工作（异步）：{}", result);
            callback.callback(result);
        });
    }

    // 异步工作方法（无回调）
    public CompletableFuture<String> workAsync(String someWork) {
        return CompletableFuture.supplyAsync(() -> {
            String result = "(" + someWork + ")";
            log.info("完成工作（异步）：{}", result);
            return result;
        });
    }
}
