package io.github.dunwu.javacore.concurrent.current.features.callback;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class Main {


    public static void main(String[] args) {

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> explosion = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                System.out.println("执行完毕！");
                return "hello";
            }
        });

        explosion.addListener(new Runnable() {
            @Override
            public void run() {
                System.out.println("Got it!");
            }
        }, service);

        Futures.addCallback(explosion, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                System.out.println(s + " world!");
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, service);

    }


}