package io.github.dunwu.javacore.reflect.dynamicproxy.flink;

/**
 * 创建实现该接口的目标对象
 */
public class ResourceManager implements ResourceManagerGateway{
    /**
     * 实现方法中打印一句话
     */
    @Override
    public void registerTaskExecutor() {
        System.out.println("注册registerTaskExecutor");
    }
}
