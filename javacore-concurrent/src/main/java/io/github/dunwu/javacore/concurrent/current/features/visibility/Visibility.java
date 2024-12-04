package io.github.dunwu.javacore.concurrent.current.features.visibility;

import io.github.dunwu.javacore.concurrent.current.features.threadPool.ThreadPoolBuilder;
import io.github.dunwu.javacore.concurrent.current.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述:
 * visibility 可见性问题
 *
 * @author zed
 * @since 2019-06-13 9:05 AM
 */
public class Visibility {
    private static long count = 0;
    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolBuilder.fixedPool().setPoolSize(2).build();

    private void add10k() {
        int idx = 0;
        while(idx++ < 10000) {
            count += 1;
        }
    }

    /**
     * 通过boolean 变量更加直观
     */
    private static boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
        System.out.println(calc());

//        System.out.println("start");
//        //线程开始
//        threadPoolExecutor.execute(() -> {
//            while(flag){
//
//            }
//            // 子线程永远不会跳出循环
//            System.out.println("stop");
//
//        });
//        ThreadUtil.sleep(100);
//        flag = false;
    }
//    private static long calc(){ TODO 为什么用线程池的写法，结果和单线程执行的结果一致呢？
//        final Visibility visibility = new Visibility();
//        threadPoolExecutor.execute(visibility::add10k);
//        threadPoolExecutor.execute(visibility::add10k);
//        /*
//         * 调用shuntDown保证线程执行完毕
//         */
//        threadPoolExecutor.shutdown();
//        return count;
//
//    }

    private static long calc() throws InterruptedException {
        final Visibility visibility = new Visibility();
        Thread th1 = new Thread(visibility::add10k);
        Thread th2 = new Thread(visibility::add10k);
        th1.start();
        th2.start();

        th1.join();
        th2.join();
        return count;
    }
}

