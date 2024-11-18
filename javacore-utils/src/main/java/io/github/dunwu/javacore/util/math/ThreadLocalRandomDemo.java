package io.github.dunwu.javacore.util.math;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomDemo {
    public static void main(String[] args) {
        // 1. 获取当前线程的 ThreadLocalRandom 实例
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 2. 生成随机整数
        int randomInt = random.nextInt(); // 返回一个任意范围的整数
        System.out.println("Random integer: " + randomInt);

        // 3. 生成指定范围的随机整数
        int boundedInt = random.nextInt(10, 20); // 返回 [10, 20) 的整数
        System.out.println("Random integer in range [10, 20): " + boundedInt);

        // 4. 生成随机浮点数
        double randomDouble = random.nextDouble(); // 返回 [0.0, 1.0) 的双精度浮点数
        System.out.println("Random double: " + randomDouble);

        // 5. 生成指定范围的随机浮点数
        double boundedDouble = random.nextDouble(1.5, 5.0); // 返回 [1.5, 5.0) 的双精度浮点数
        System.out.println("Random double in range [1.5, 5.0): " + boundedDouble);

        // 6. 生成随机布尔值
        boolean randomBoolean = random.nextBoolean(); // 返回 true 或 false
        System.out.println("Random boolean: " + randomBoolean);

        // 7. 生成指定范围的随机长整数
        long randomLong = random.nextLong(1000, 5000); // 返回 [1000, 5000) 的长整数
        System.out.println("Random long in range [1000, 5000): " + randomLong);

        // 8. 使用多线程生成随机数
        Runnable task = () -> {
            ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
            System.out.println("Thread " + Thread.currentThread().getName() + " random int: " + threadRandom.nextInt(100));
        };

        // 创建多个线程
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
    }
}
