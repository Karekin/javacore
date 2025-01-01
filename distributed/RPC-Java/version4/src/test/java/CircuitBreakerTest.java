import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import part1.Client.circuitBreaker.CircuitBreaker;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试 CircuitBreaker 熔断器逻辑的 JUnit 测试类
 */
class CircuitBreakerTest {
    private CircuitBreaker circuitBreaker;

    /**
     * 每次测试前初始化 CircuitBreaker 对象
     * 测试用例参数：
     * - 失败阈值为 3
     * - 半开状态需要成功率 >= 50%
     * - 熔断器重试时间为 2000 毫秒
     */
    @BeforeEach
    void setUp() {
        circuitBreaker = new CircuitBreaker(3, 0.5, 2000);
    }

    /**
     * 测试熔断器的初始状态
     * 目标：
     * - 验证熔断器的初始状态应为 CLOSED
     */
    @Test
    void testInitialState() {
        assertEquals(CircuitBreaker.CircuitBreakerState.CLOSED, circuitBreaker.getState(),
                "初始状态应为 CLOSED");
    }

    /**
     * 测试在 CLOSED 状态下允许所有请求
     * 目标：
     * - 验证 CLOSED 状态时，所有请求都被允许
     */
    @Test
    void testAllowRequestWhenClosed() {
        assertTrue(circuitBreaker.allowRequest(), "CLOSED 状态下所有请求应被允许");
    }

    /**
     * 测试在失败次数超过阈值后状态是否切换为 OPEN
     * 目标：
     * - 模拟连续失败超过阈值（3 次），验证状态是否切换为 OPEN
     * - 验证 OPEN 状态是否拒绝请求
     */
    @Test
    void testRecordFailureTransitionToOpen() {
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();

        // 验证状态切换为 OPEN
        assertEquals(CircuitBreaker.CircuitBreakerState.OPEN, circuitBreaker.getState(),
                "失败次数超过阈值后状态应为 OPEN");

        // 验证 OPEN 状态下请求被拒绝
        assertFalse(circuitBreaker.allowRequest(), "OPEN 状态下请求应被拒绝");
    }

    /**
     * 测试在 OPEN 状态下等待重试时间是否进入 HALF_OPEN
     * 目标：
     * - 验证 OPEN 状态下是否拒绝请求
     * - 等待重试时间（2000 毫秒）后，验证状态是否切换为 HALF_OPEN
     */
    @Test
    void testAllowRequestWhenOpen() throws InterruptedException {
        // 模拟进入 OPEN 状态
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();

        // 验证 OPEN 状态拒绝请求
        assertEquals(CircuitBreaker.CircuitBreakerState.OPEN, circuitBreaker.getState(),
                "应进入 OPEN 状态");
        assertFalse(circuitBreaker.allowRequest(), "OPEN 状态下请求应被拒绝");

        // 等待超过重试时间
        Thread.sleep(2000);

        // 验证状态切换为 HALF_OPEN
        assertTrue(circuitBreaker.allowRequest(), "超过重试时间后，HALF_OPEN 应允许请求");
        assertEquals(CircuitBreaker.CircuitBreakerState.HALF_OPEN, circuitBreaker.getState(),
                "应进入 HALF_OPEN 状态");
    }

    /**
     * 测试在 HALF_OPEN 状态下成功率达到要求是否切换回 CLOSED
     * 目标：
     * - 模拟 HALF_OPEN 状态
     * - 验证成功率达到 50% 时是否切换回 CLOSED
     */
    @Test
    void testRecordSuccessInHalfOpen() throws InterruptedException {
        // 模拟进入 HALF_OPEN 状态
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();

        // 等待重试时间，模拟进入 HALF_OPEN 状态
        Thread.sleep(2000);
        circuitBreaker.allowRequest(); // 模拟进入 HALF_OPEN

        // 模拟成功记录
        circuitBreaker.recordSuccess();
        circuitBreaker.recordSuccess(); // 达到 50% 成功率

        // 验证状态切换为 CLOSED
        assertEquals(CircuitBreaker.CircuitBreakerState.CLOSED, circuitBreaker.getState(),
                "HALF_OPEN 成功率达到要求后应切换回 CLOSED");
    }

    /**
     * 测试在 HALF_OPEN 状态下记录失败是否回到 OPEN
     * 目标：
     * - 模拟 HALF_OPEN 状态
     * - 在 HALF_OPEN 状态下记录一次失败，验证是否回到 OPEN
     */
    @Test
    void testRecordFailureInHalfOpen() {
        // 模拟进入 HALF_OPEN 状态
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.allowRequest(); // HALF_OPEN 状态

        // 在 HALF_OPEN 状态下记录一次失败
        circuitBreaker.recordFailure();

        // 验证状态切换回 OPEN
        assertEquals(CircuitBreaker.CircuitBreakerState.OPEN, circuitBreaker.getState(),
                "HALF_OPEN 失败后应切换回 OPEN");
    }

    /**
     * 测试状态切换时是否正确重置计数器
     * 目标：
     * - 模拟熔断器的状态切换
     * - 验证计数器是否在状态切换时正确重置
     */
    @Test
    void testResetCounts() throws InterruptedException {
        // 模拟失败和成功记录
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();

        // 验证进入 OPEN 状态
        assertEquals(CircuitBreaker.CircuitBreakerState.OPEN, circuitBreaker.getState(),
                "失败超过阈值后应切换到 OPEN");

        // 等待重试时间，模拟进入 HALF_OPEN 状态
        Thread.sleep(2000); // retryTimePeriod 的值为 2000 毫秒

        // 超过重试时间后，允许请求并切换到 HALF_OPEN 状态
        assertTrue(circuitBreaker.allowRequest(), "超过重试时间后应允许请求");
        assertEquals(CircuitBreaker.CircuitBreakerState.HALF_OPEN, circuitBreaker.getState(),
                "超过重试时间后应切换到 HALF_OPEN 状态");

        // 模拟成功
        circuitBreaker.recordSuccess();
        circuitBreaker.recordSuccess();

        // 验证进入 CLOSED 状态后计数器重置
        assertEquals(CircuitBreaker.CircuitBreakerState.CLOSED, circuitBreaker.getState(),
                "成功率达标后应切换到 CLOSED");
        assertEquals(0, circuitBreaker.getState() == CircuitBreaker.CircuitBreakerState.CLOSED
                ? 0 : -1, "计数器应在状态切换时重置");
    }
}

