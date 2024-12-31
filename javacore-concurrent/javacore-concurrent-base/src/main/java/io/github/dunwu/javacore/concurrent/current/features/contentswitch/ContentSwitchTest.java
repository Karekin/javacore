package io.github.dunwu.javacore.concurrent.current.features.contentswitch;

import io.github.dunwu.javacore.concurrent.current.features.threadPool.ThreadPoolBuilder;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述：上下文切换测试类
 *
 * 1、结论：循环数量在百万级别时并发处理速度才会领先，说明上下文切换的存在以及线程创建的成本。
 * 2、使用Lmbench3工具测量上下文切换时长，使用vmstat查看上下文切换次数和上下文速度，目标为1000次/s。
 * 3、减少上下文切换的方法：
 *    - 1）无锁并发编程：多线程锁竞争会引起切换，可以通过数据ID按照Hash算法取模分段，不同线程处理不同数据，避免使用锁。
 *    - 2）CAS算法：通过无锁的方式进行线程安全的操作。
 *    - 3）协程：在单线程里实现多任务调度，并在单线程里维持多个任务的切换。
 *    - 4）使用最少线程：减少线程的创建和上下文切换。
 *
 * @author zed
 */
public class ContentSwitchTest {
    // 定义一个常量，用于循环的次数，模拟大量的计算操作
    private static final long COUNT = 10000000000L;

    // 创建一个固定大小线程池，线程池大小为1
    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolBuilder.fixedPool().setPoolSize(1).build();

    public static void main(String[] args){
        // 执行并发测试
        concurrency();

        // 执行串行测试
        serial();
    }

    /**
     * 测试并发执行的场景，使用线程池执行任务
     */
    private static void concurrency(){
        // 记录开始时间
        long start = System.currentTimeMillis();

        // 在线程池中提交一个任务，该任务进行 COUNT 次循环，执行加法操作
        threadPoolExecutor.execute(() -> {
            int a = 0;
            // 模拟大量的计算操作
            for (long i = 0; i < COUNT; i++) {
                a += 5; // 累加操作
            }
            // 打印结果
            System.out.println(a);
        });

        // 主线程进行类似的计算操作，执行减法
        int b = 0;
        for (long i = 0; i < COUNT; i++) {
            b--; // 累减操作
        }

        // 关闭线程池，等待所有任务执行完毕
        threadPoolExecutor.shutdown();

        // 计算并发执行的总耗时
        long time = System.currentTimeMillis() - start;

        // 打印并发执行的耗时和变量b的结果
        System.out.println("concurrency :" + time + "ms, b=" + b);
    }

    /**
     * 测试串行执行的场景，在主线程中执行所有任务
     */
    private static void serial(){
        // 记录开始时间
        long start = System.currentTimeMillis();

        // 串行执行第一个循环，进行加法操作
        int a = 0;
        for (long i = 0; i < COUNT; i++) {
            a += 5; // 累加操作
        }

        // 串行执行第二个循环，进行减法操作
        int b = 0;
        for (long i = 0; i < COUNT; i++) {
            b--; // 累减操作
        }

        // 计算串行执行的总耗时
        long time = System.currentTimeMillis() - start;

        // 打印串行执行的耗时和变量a、b的结果
        System.out.println("serial:" + time + "ms, b=" + b + ", a=" + a);
    }
}
