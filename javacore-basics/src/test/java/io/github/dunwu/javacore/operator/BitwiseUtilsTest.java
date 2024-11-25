package io.github.dunwu.javacore.operator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BitwiseUtilsTest {

    @Test
    void testAnd() {
        // 测试按位与运算
        assertEquals(12, BitwiseUtils.and(60, 13)); // 0011 1100 & 0000 1101 = 0000 1100
    }

    @Test
    void testOr() {
        // 测试按位或运算
        assertEquals(61, BitwiseUtils.or(60, 13)); // 0011 1100 | 0000 1101 = 0011 1101
    }

    @Test
    void testXor() {
        // 测试按位异或运算
        assertEquals(49, BitwiseUtils.xor(60, 13)); // 0011 1100 ^ 0000 1101 = 0011 0001
    }

    @Test
    void testNot() {
        // 测试按位取反运算
        assertEquals(-61, BitwiseUtils.not(60)); // ~0011 1100 = 1100 0011
    }

    @Test
    void testLeftShift() {
        // 测试左移运算
        assertEquals(240, BitwiseUtils.leftShift(60, 2)); // 0011 1100 << 2 = 1111 0000
    }

    @Test
    void testRightShift() {
        // 测试算术右移运算
        assertEquals(15, BitwiseUtils.rightShift(60, 2)); // 0011 1100 >> 2 = 0000 1111
    }

    @Test
    void testUnsignedRightShift() {
        // 测试无符号右移运算
        assertEquals(15, BitwiseUtils.unsignedRightShift(60, 2)); // 0011 1100 >>> 2 = 0000 1111
    }

    @Test
    void testIsOdd() {
        // 测试判断奇数
        assertTrue(BitwiseUtils.isOdd(3)); // 3 是奇数
        assertFalse(BitwiseUtils.isOdd(4)); // 4 是偶数
    }

    @Test
    void testIsPowerOfTwo() {
        // 测试判断是否为 2 的幂
        assertTrue(BitwiseUtils.isPowerOfTwo(4)); // 4 是 2 的幂
        assertFalse(BitwiseUtils.isPowerOfTwo(6)); // 6 不是 2 的幂
    }

    @Test
    void testSwap() {
        // 测试交换两个整数
        int[] swapped = BitwiseUtils.swap(3, 5);
        assertArrayEquals(new int[]{5, 3}, swapped); // 交换后结果为 {5, 3}
    }

    @Test
    void testLowestOneBit() {
        // 测试获取最低位的 1
        assertEquals(2, BitwiseUtils.lowestOneBit(18)); // 10010 -> 最低位的 1 是 2
    }

    @Test
    void testClearLowestOneBit() {
        // 测试清除最低位的 1
        assertEquals(16, BitwiseUtils.clearLowestOneBit(18)); // 10010 -> 清除最低位的 1 得到 10000
    }

    @Test
    void testHaveSameSign() {
        // 测试判断符号是否相同
        assertTrue(BitwiseUtils.haveSameSign(5, 10)); // 正数
        assertTrue(BitwiseUtils.haveSameSign(-5, -10)); // 负数
        assertFalse(BitwiseUtils.haveSameSign(5, -10)); // 一正一负
    }

    @Test
    void testMedianThree() {
        // 测试选取三个候选元素的中位数
        int[] nums = {3, 1, 2};
        assertEquals(2, BitwiseUtils.medianThree(nums, 0, 1, 2)); // 获取三个元素中的中位数
    }

    @Test
    void testAbsoluteValue() {
        // 测试计算绝对值
        assertEquals(5, BitwiseUtils.absoluteValue(-5)); // 绝对值为 5
        assertEquals(5, BitwiseUtils.absoluteValue(5)); // 绝对值为 5
    }

    @Test
    void testIsBinaryPalindrome() {
        // 测试是否是二进制回文
        assertTrue(BitwiseUtils.isBinaryPalindrome(9)); // 9 的二进制 1001 是回文
        assertFalse(BitwiseUtils.isBinaryPalindrome(10)); // 10 的二进制 1010 不是回文
    }

    @Test
    void testMax() {
        // 测试取两个数的最大值
        assertEquals(10, BitwiseUtils.max(5, 10)); // 最大值是 10
    }

    @Test
    void testMin() {
        // 测试取两个数的最小值
        assertEquals(5, BitwiseUtils.min(5, 10)); // 最小值是 5
    }

    @Test
    void testIsZero() {
        // 测试判断是否为零
        assertTrue(BitwiseUtils.isZero(0)); // 是零
        assertFalse(BitwiseUtils.isZero(5)); // 不是零
    }

    @Test
    void testModByPowerOfTwo() {
        // 测试 2 的幂取模
        assertEquals(3, BitwiseUtils.modByPowerOfTwo(11, 8)); // 11 % 8 = 3
    }

    @Test
    void testFlipBits() {
        // 测试位翻转
        assertEquals(-6, BitwiseUtils.flipBits(5)); // ~0000 0101 = 1111 1010
    }

    @Test
    void testCountOnes() {
        // 测试统计二进制中 1 的个数
        assertEquals(3, BitwiseUtils.countOnes(11)); // 11 的二进制 1011 有 3 个 1
    }
}

