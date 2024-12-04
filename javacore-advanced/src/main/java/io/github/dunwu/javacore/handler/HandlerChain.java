package io.github.dunwu.javacore.handler;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 1. 测试流程
 * 启动项目：运行 HandlerApplication，启动 Spring Boot 服务。
 * 测试请求：使用浏览器或工具（如 Postman）发送以下请求，GET <a href="http://localhost:8080/process?input=test_input">...</a>
 * 响应结果：FirstTableSupplementHandler processed: test_input SecondExampleHandler processed: test_input
 * 2. 代码说明
 * 自动注册：
 *      @Component 标记了处理器，使其成为 Spring 管理的 Bean。
 *      @Order 控制了处理器的执行顺序。
 * 处理链：
 *      HandlerChain 自动收集实现 IHandler 接口的所有 Bean，并按 @Order 注解排序。
 * 可扩展性：
 *      添加新的处理器只需实现 IHandler 接口，并添加 @Component 注解，无需修改现有代码。
 * 3. 总结
 * 这个 Demo 展示了如何通过 Spring 的自动装配机制实现处理器链的自动注册和执行逻辑，便于动态扩展和维护。
 *
 */
@Component
public class HandlerChain {

    private final List<IHandler> handlerList;

    public HandlerChain(List<IHandler> handlerList) {
        this.handlerList = handlerList;
    }

    public void handle(String request, StringBuilder response) {
        handlerList.forEach(handler -> handler.handleRequest(request, response));
    }
}

