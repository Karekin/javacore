package io.github.dunwu.javacore.util.math;

import java.util.Random;

public class ProbabilityDemo {

    public static void main(String[] args) {
        Random random = new Random();

        // 示例：事件发生的概率为 40%（0 到 39）
        int probability = 40;

        // 重复测试 10 次
        for (int i = 1; i <= 10; i++) {
            // 生成一个 [0, 99] 的随机整数
            int randomValue = random.nextInt(100);

            // 如果随机值小于概率阈值，表示事件发生
            if (randomValue < probability) {
                System.out.println("事件发生 (Trial " + i + "): 随机值 = " + randomValue);
            } else {
                System.out.println("事件未发生 (Trial " + i + "): 随机值 = " + randomValue);
            }
        }
    }
}

