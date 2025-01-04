package io.github.dunwu.javacore.concurrent.current.features.callback;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author yangzijing
 */
@Slf4j
public class ImperBoss {

    private ImperWorker worker;

    public ImperBoss(ImperWorker worker) {
        this.worker = worker;
    }

    public void makeBigDeals(final String someDetail) throws ExecutionException, InterruptedException {
        log.info("分配工作...");
        String result = worker.work(someDetail);
        log.info("老板拿到结果: {}", result);
        log.info("老板下班回家了。。。。");
    }

}
