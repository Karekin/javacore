package io.github.dunwu.javacore.reflect.dynamicproxy.flink;

/**
 * 模拟Flink为代理目标对象定义一个接口
 */
public interface ResourceManagerGateway {
    /**
     * 定义一个注册方法
     */
    void registerTaskExecutor();
}
