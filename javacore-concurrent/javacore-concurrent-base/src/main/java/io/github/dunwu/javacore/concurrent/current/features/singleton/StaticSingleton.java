package io.github.dunwu.javacore.concurrent.current.features.singleton;

/**
 * @author zed
 */
public class StaticSingleton {

    private StaticSingleton(){
    }
    private static class StaticSingletonHolder{
        private static StaticSingleton singleton= new StaticSingleton();
    }
    public static StaticSingleton getInstance(){
        return StaticSingletonHolder.singleton;
    }
}
