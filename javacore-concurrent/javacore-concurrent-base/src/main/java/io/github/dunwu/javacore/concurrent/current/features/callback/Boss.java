package io.github.dunwu.javacore.concurrent.current.features.callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class Boss implements Callback<String> {

    private final Worker worker;

    public Boss(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void callback(String s) {
        log.info("boss got the feedback from worker: {}", s);
    }

    public void makeBigDeals(final String someDetail) {
        log.info("分配工作...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                worker.workAsync(Boss.this, someDetail);
            }
        }).start();
        log.info("分配完工作。");
        log.info("老板下班回家了。。。。");
    }

}