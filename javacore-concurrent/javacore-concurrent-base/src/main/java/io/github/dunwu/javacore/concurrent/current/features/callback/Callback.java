package io.github.dunwu.javacore.concurrent.current.features.callback;

/**
 * @author yangzijing
 */
public interface Callback<T> {

    /**
     * 具体实现
     * @param t
     */
    public void callback(T t);

}
