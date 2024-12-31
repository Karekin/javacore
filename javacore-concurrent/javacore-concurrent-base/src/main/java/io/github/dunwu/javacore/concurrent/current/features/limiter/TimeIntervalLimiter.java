package io.github.dunwu.javacore.concurrent.current.features.limiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 描述:
 * TimeIntervalLimiter 实现了基于时间间隔的限流器，
 * 它允许在一定的时间窗口内只允许一次操作，防止短时间内的重复操作。
 * 主要用于控制操作的频率，例如：限流、节流等场景。
 *
 * @author zed
 */
public class TimeIntervalLimiter {

    // 使用 AtomicLong 来保存上次成功获取许可的时间，确保线程安全
    private final AtomicLong lastTimeAtom = new AtomicLong(0);

    // 时间窗口大小（单位：毫秒），即两次操作之间的最小间隔
    private final long windowSizeMillis;

    /**
     * 构造函数，用于初始化限流器。
     * @param interval 时间间隔的大小
     * @param timeUnit 时间单位（如：秒、毫秒等）
     */
    public TimeIntervalLimiter(long interval, TimeUnit timeUnit) {
        // 将时间间隔转换为毫秒表示，windowSizeMillis 代表两次操作之间的最小间隔
        this.windowSizeMillis = timeUnit.toMillis(interval);
    }

    /**
     * 尝试获取许可，如果时间间隔足够长，则允许操作。
     * @return 如果当前时间距离上次操作的时间间隔大于等于设定的窗口大小，则返回 true，
     *         否则返回 false，并且不更新 lastTimeAtom。
     */
    public boolean tryAcquire() {
        // 获取当前系统时间（毫秒）
        long currentTime = System.currentTimeMillis();

        // 获取上次成功操作的时间
        long lastTime = lastTimeAtom.get();

        // 如果当前时间和上次操作时间的间隔大于等于窗口大小，
        // 且成功地将 lastTimeAtom 的值更新为当前时间，表示允许操作。
        return currentTime - lastTime >= windowSizeMillis && lastTimeAtom.compareAndSet(lastTime, currentTime);
    }
}
