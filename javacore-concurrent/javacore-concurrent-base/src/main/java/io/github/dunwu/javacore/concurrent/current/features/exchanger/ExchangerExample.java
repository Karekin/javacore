package io.github.dunwu.javacore.concurrent.current.features.exchanger;

import io.github.dunwu.javacore.concurrent.current.features.threadPool.ThreadPoolBuilder;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述:
 * Exchanger 示例：Exchanger 用于两个线程之间的数据交换。
 * 这个示例演示了如何使用 Exchanger 来在两个线程之间交换数据。
 * Exchanger 是一个用于线程间交换数据的工具类，它提供了一个 `exchange()` 方法，
 * 允许一个线程传递数据到另一个线程，并等待另一个线程的响应。
 *
 * @author zed
 * @since 2019-07-03 5:39 PM
 */
public class ExchangerExample {
    // 创建一个 Exchanger 实例，用于在线程之间交换 String 类型的数据
    private static final Exchanger<String> EXCHANGER = new Exchanger<>();

    // 创建一个线程池，线程池大小为 2
    private static final ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.fixedPool().setPoolSize(2).build();

    public static void main(String[] args) {
        // 提交第一个线程任务到线程池
        poolExecutor.execute(() -> {
            try {
                // 第一个线程准备要交换的数据
                String s = "SomethingAndA";

                // 通过 Exchanger 的 exchange 方法将数据传递给另一个线程
                EXCHANGER.exchange(s);

            } catch (InterruptedException e) {
                // 捕获并打印异常信息
                e.printStackTrace();
            }
        });

        // 提交第二个线程任务到线程池
        poolExecutor.execute(() -> {
            try {
                // 第二个线程准备交换的数据
                String s1 = "SomethingAndB";

                // 第二个线程调用 exchange 方法，将数据传递给第一个线程并等待第一个线程的数据
                String s = EXCHANGER.exchange("s1");

                // 打印交换后的数据，检查两个线程的数据是否匹配
                System.out.println("s 和 s1 值是否相等：" + s1.equals(s) + ", s：" + s + ", s1：" + s1);

            } catch (InterruptedException e) {
                // 捕获并打印异常信息
                e.printStackTrace();
            }
        });

        // 关闭线程池
        poolExecutor.shutdown();
    }
}
