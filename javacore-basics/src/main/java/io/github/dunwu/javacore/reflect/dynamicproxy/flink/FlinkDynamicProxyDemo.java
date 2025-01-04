package io.github.dunwu.javacore.reflect.dynamicproxy.flink;

import java.lang.reflect.Proxy;

public class FlinkDynamicProxyDemo {
    public static void main(String[] args) {
        /** 创建目标对象 */
        ResourceManager myObject = new ResourceManager();
        /** 创建InvocationHandler */
        PekkoInvocationHandler handler = new PekkoInvocationHandler(myObject);
        /** 调用 Proxy.newProxyInstance静态方法创建动态代理类 */
        ResourceManagerGateway proxy = (ResourceManagerGateway) Proxy.newProxyInstance(
                ResourceManagerGateway.class.getClassLoader(),
                new Class<?>[] { ResourceManagerGateway.class },
                handler);
        /** 调用registerTaskExecutor 注册方法最终会调用PekkoInvocationHandler的invoke方法 */
        proxy.registerTaskExecutor();
    }
}
