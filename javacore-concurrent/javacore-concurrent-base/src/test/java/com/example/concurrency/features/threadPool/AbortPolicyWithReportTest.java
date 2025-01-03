package com.example.concurrency.features.threadPool;

import io.github.dunwu.javacore.concurrent.current.features.threadPool.AbortPolicyWithReport;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zed
 * AbortPolicyWithReportTest
 */
public class AbortPolicyWithReportTest {

    @Test
    public void jStackDumpTest() throws InterruptedException {
        AbortPolicyWithReport abortPolicyWithReport = new AbortPolicyWithReport("test");

        try {
            abortPolicyWithReport.rejectedExecution(() -> System.out.println("hello"), (ThreadPoolExecutor) Executors.newFixedThreadPool(1));
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
        Thread.sleep(1000);

    }
}