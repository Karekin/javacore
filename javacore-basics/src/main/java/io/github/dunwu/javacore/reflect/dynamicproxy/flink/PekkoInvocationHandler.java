package io.github.dunwu.javacore.reflect.dynamicproxy.flink;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 创建实现InvocationHandler接口的类
 */
public class PekkoInvocationHandler implements InvocationHandler {
    /**目标对象*/
    private Object target;

    public PekkoInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     *
     * 在invoke中调用内部方法invokeRpc
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return invokeRpc(method,args);
    }

    /**
     * 在invokeRpc中实现自己的逻辑，比如向ResourceManager发送pekko的请求
     * flink内部实现的时候会将调用的代理类和方法封装成RpcInvocation 调用ask方法发送给PekkoRpcActor接收到消息
     * 内部调用HandlerMessage处理不同类型的请求然后通过java反射调用最终调用传递给ResourceManager.registerTaskExecutor方法
     *
     * @param method
     * @param args
     * @return
     * @throws Exception
     */
    private Object invokeRpc(Method method, Object[] args) throws Exception {
        System.out.println("调用pekko ask方法向ResourceManager发送调用的方法");
        Object result = method.invoke(target,args);
        System.out.println("结束调用");
        return result;
    }
}
