package io.github.dunwu.javacore.util.math;

import java.util.Random;

import java.util.Random;

public class RandomDemo {
    public static void main(String[] args) {
        // 1. 创建 Random 实例
        Random random = new Random();

        // 2. 生成随机整数
        int randomInt = random.nextInt(); // 返回一个任意范围的整数
        System.out.println("Random integer: " + randomInt);

        // 3. 生成指定范围的随机整数
        int boundedInt = random.nextInt(50); // 返回 [0, 50) 的整数
        System.out.println("Random integer in range [0, 50): " + boundedInt);

        // 4. 生成随机浮点数
        float randomFloat = random.nextFloat(); // 返回 [0.0, 1.0) 的浮点数
        System.out.println("Random float: " + randomFloat);

        // 5. 生成随机双精度浮点数
        double randomDouble = random.nextDouble(); // 返回 [0.0, 1.0) 的双精度浮点数
        System.out.println("Random double: " + randomDouble);

        // 6. 生成随机布尔值
        boolean randomBoolean = random.nextBoolean(); // 返回 true 或 false
        System.out.println("Random boolean: " + randomBoolean);

        // 7. 生成随机字节数组
        byte[] randomBytes = new byte[5];
        random.nextBytes(randomBytes); // 填充字节数组
        System.out.print("Random bytes: ");
        for (byte b : randomBytes) {
            System.out.print(b + " ");
        }
        System.out.println();

        // 8. 使用种子生成随机数（种子固定，生成的随机数序列是确定的）
        Random seededRandom = new Random(12345); // 设置种子
        System.out.println("Random integer with seed: " + seededRandom.nextInt());
        System.out.println("Random double with seed: " + seededRandom.nextDouble());
    }
}

