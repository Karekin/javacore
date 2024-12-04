package io.github.dunwu.javacore.concurrent.current.features.singleton;

/**
 * 描述:
 * 双重检查锁实现单例
 *
 * @author zed
 */
public class Singleton {
    /**
     * volatile保证可见性，防止指令重排
     */
    private static volatile Singleton singleton;

    /**
     * 私有无参构造保证唯一入口
     */
    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    /*
                        多个线程同时调用 getInstance 方法，
                        如果发生了指令重排并且在实例初始化之前发生了线程切换，
                        那么可能导致另一个线程出现空指针异常。
                        因此，需要使用 volatile 避免指令重排
                     */
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}

