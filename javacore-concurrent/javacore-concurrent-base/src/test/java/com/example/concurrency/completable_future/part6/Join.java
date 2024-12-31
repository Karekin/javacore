package com.example.concurrency.completable_future.part6;

import com.example.concurrency.completable_future.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class Join extends Demo {

    @Test
    public void testJoin() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));
        assertEquals("value", future.join()); // throws no checked exceptions
    }
}
