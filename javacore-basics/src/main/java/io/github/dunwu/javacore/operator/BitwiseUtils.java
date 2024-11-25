package io.github.dunwu.javacore.operator;

/**
 * 位运算工具类，包含常见的位运算技巧方法。
 */
public class BitwiseUtils {
    /* ******************************** 基础篇 ************************************* */

    /**
     * 按位与运算。
     *
     * @param a 第一个整数
     * @param b 第二个整数
     * @return a 和 b 的按位与结果
     */
    public static int and(int a, int b) {
        return a & b;
    }

    /**
     * 按位或运算。
     *
     * @param a 第一个整数
     * @param b 第二个整数
     * @return a 和 b 的按位或结果
     */
    public static int or(int a, int b) {
        return a | b;
    }

    /**
     * 按位异或运算。
     *
     * @param a 第一个整数
     * @param b 第二个整数
     * @return a 和 b 的按位异或结果
     */
    public static int xor(int a, int b) {
        return a ^ b;
    }

    /**
     * 按位取反运算。
     *
     * @param a 输入整数
     * @return a 的按位取反结果
     */
    public static int not(int a) {
        return ~a;
    }

    /**
     * 左移操作（将二进制位左移 n 位）。
     *
     * @param a 输入整数
     * @param n 左移的位数
     * @return 左移 n 位后的结果
     */
    public static int leftShift(int a, int n) {
        return a << n;
    }

    /**
     * 算术右移操作（将二进制位右移 n 位，保留符号位）。
     *
     * @param a 输入整数
     * @param n 右移的位数
     * @return 算术右移 n 位后的结果
     */
    public static int rightShift(int a, int n) {
        return a >> n;
    }

    /**
     * 无符号右移操作（将二进制位右移 n 位，不保留符号位）。
     *
     * @param a 输入整数
     * @param n 右移的位数
     * @return 无符号右移 n 位后的结果
     */
    public static int unsignedRightShift(int a, int n) {
        return a >>> n;
    }

    /* ******************************** 技巧篇 ************************************* */

    /**
     * 判断一个整数是否为奇数。
     *
     * @param num 输入整数
     * @return 如果是奇数返回 true，否则返回 false
     */
    public static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    /**
     * 判断一个整数是否是 2 的幂。
     *
     * @param num 输入整数
     * @return 如果是 2 的幂返回 true，否则返回 false
     */
    public static boolean isPowerOfTwo(int num) {
        return num > 0 && (num & (num - 1)) == 0;
    }

    /**
     * 交换两个整数的值（不使用临时变量）。
     *
     * @param a 第一个整数
     * @param b 第二个整数
     * @return 交换后的两个整数
     */
    public static int[] swap(int a, int b) {
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        return new int[]{a, b};
    }

    /**
     * 获取整数二进制表示中最低位的 1。
     *
     * @param num 输入整数
     * @return 整数最低位的 1
     */
    public static int lowestOneBit(int num) {
        return num & -num;
    }

    /**
     * 清除整数二进制表示中最低位的 1。
     *
     * @param num 输入整数
     * @return 清除最低位的 1 后的结果
     */
    public static int clearLowestOneBit(int num) {
        return num & (num - 1);
    }

    /**
     * 判断两个整数是否符号相同。
     *
     * @param x 第一个整数
     * @param y 第二个整数
     * @return 如果符号相同返回 true，否则返回 false
     */
    public static boolean haveSameSign(int x, int y) {
        return (x ^ y) >= 0;
    }

    /**
     * 选取三个候选元素的中位数
     * @param nums 数组
     * @param left 第一个整数
     * @param mid 第二个整数
     * @param right 第三个整数
     * @return 返回中位数
     */
    public static int medianThree(int[] nums, int left, int mid, int right) {
        // 此处使用异或运算来简化代码
        // 异或规则为 0 ^ 0 = 1 ^ 1 = 0, 0 ^ 1 = 1 ^ 0 = 1
        if ((nums[left] < nums[mid]) ^ (nums[left] < nums[right]))
            return left;
        else if ((nums[mid] < nums[left]) ^ (nums[mid] < nums[right]))
            return mid;
        else
            return right;
    }



    /**
     * 快速计算整数的绝对值（不适用于 Integer.MIN_VALUE）。
     *
     * @param num 输入整数
     * @return 绝对值
     */
    public static int absoluteValue(int num) {
        int mask = num >> 31; // 获取符号位，负数为 -1，正数为 0
        return (num + mask) ^ mask;
    }

    /**
     * 判断一个整数是否是二进制回文。
     *
     * @param num 输入整数
     * @return 如果是二进制回文返回 true，否则返回 false
     */
    public static boolean isBinaryPalindrome(int num) {
        int reversed = 0, original = num;
        while (num != 0) {
            reversed = (reversed << 1) | (num & 1);
            num >>= 1;
        }
        return reversed == original;
    }

    /**
     * 取两个整数的最大值。
     *
     * @param a 第一个整数
     * @param b 第二个整数
     * @return 两个数中的最大值
     */
    public static int max(int a, int b) {
        return b + ((a - b) & ((a - b) >> 31 ^ -1));
    }

    /**
     * 取两个整数的最小值。
     *
     * @param a 第一个整数
     * @param b 第二个整数
     * @return 两个数中的最小值
     */
    public static int min(int a, int b) {
        return a - ((a - b) & ((a - b) >> 31 ^ -1));
    }

    /**
     * 判断一个整数是否为零。
     *
     * @param num 输入整数
     * @return 如果是零返回 true，否则返回 false
     */
    public static boolean isZero(int num) {
        return (num | -num) == 0;
    }

    /**
     * 对 2 的幂取模（快速计算）。
     *
     * @param num 输入整数
     * @param powerOfTwo 2 的幂
     * @return 取模结果
     */
    public static int modByPowerOfTwo(int num, int powerOfTwo) {
        return num & (powerOfTwo - 1);
    }

    /**
     * 翻转整数的所有位。
     *
     * @param num 输入整数
     * @return 位翻转后的结果
     */
    public static int flipBits(int num) {
        return ~num;
    }

    /**
     * 统计整数的二进制表示中 1 的个数。
     *
     * @param num 输入整数
     * @return 1 的个数
     */
    public static int countOnes(int num) {
        int count = 0;
        while (num != 0) {
            num &= (num - 1); // 清除最低位的 1
            count++;
        }
        return count;
    }


}
