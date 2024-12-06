package io.github.dunwu.javacore.autowiring.lineage.handlers.chain;

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
 * 4. 责任链模式
 * 本 Demo 都属于 责任链模式（Chain of Responsibility）的实现。
 * 这种设计模式的核心思想是将请求沿着一系列的处理器传递，每个处理器可以决定是否处理请求，或者将其传递给下一个处理器。
 * ①、责任链模式的核心特点
 * 1). 处理器的独立性：
 *    - 每个处理器专注于自身的逻辑处理，不需要关心其他处理器的存在。
 *    - 通过定义接口（如 IHandler），可以扩展更多的处理器，而无需修改责任链的实现。
 * 2). 链式处理：
 *    - 请求从链头传递到链尾，每个处理器可以选择处理请求或跳过处理。
 * 3). 动态组合：
 *    - 处理器的数量和顺序可以动态调整（如通过 @Order 注解控制顺序）。
 * ②、你的代码中责任链的体现
 *
 * 1). DefaultHandlerChain 是责任链的管理者：
 *    - 它将请求 (SqlRequestContext) 和响应 (SqlResponseContext) 传递给链中的每一个处理器。
 *    - 使用 List<IHandler> 管理多个处理器，并通过循环依次调用它们的 handleRequest 方法。
 * 2). 每个处理器是链上的一个节点：
 *    - FirstTableSupplementHandler 是 IHandler 的实现类，定义了自己的处理逻辑。
 *    - 通过 @Order 确保它在责任链中的位置。
 * 3）. 动态扩展能力：
 *    - Spring 自动收集 @Component 标记的 IHandler，将它们注册到链中，这使得责任链的处理器可以动态增加或减少。
 *    - 顺序由 @Order 控制，无需显式修改链的实现代码。
 *
 * ③、责任链模式的优点
 * 1). 解耦：
 *    - 发送者和接收者之间完全解耦，发送者只需要将请求交给链头即可。
 * 2). 动态组合：
 *    - 可以在运行时灵活调整责任链上的处理器顺序和数量。
 * 3). 代码清晰：
 *    - 每个处理器只负责自己的任务，职责单一，便于理解和维护。
 * 4). 扩展性强：
 *    - 添加新处理器时，不需要修改现有代码，只需实现 IHandler 并注册即可。
 *
 * ④、在你的场景下的应用
 * 你的代码场景（比如解析 SQL 请求、补充字段信息）中，责任链模式的优势尤为明显：
 * - 每个 IHandler 实现类负责一个特定的任务，例如补全字段、校验 SQL。
 * - DefaultHandlerChain 将这些任务串联起来，无需让调用者关心具体的处理细节。
 * - 当业务逻辑需要调整时，只需增加或修改特定的 IHandler 实现即可。
 *
 * ⑤、责任链的局限性
 * 尽管责任链模式非常灵活，但也有一定局限性：
 * 1). 性能问题：
 *    - 如果链条很长，且每个处理器都需要执行，会导致性能开销。
 * 2). 调试困难：
 *    - 请求经过多个处理器后，可能难以定位某一环节的处理逻辑。
 * 3). 请求无法终止：
 *    - 如果设计中没有机制让链条的某个处理器中断请求，所有处理器都会被依次执行，即使中途处理已经完成。
 * 在你的代码中，可以通过检查 response 的状态或添加特定的中断机制来优化。
 *
 * ⑥、总结
 * 责任链模式非常适合像 SQL 解析这种任务链式处理的场景，它能帮助你更好地组织代码逻辑，同时保持扩展性和灵活性。
 *
 */
@Component
public class HandlerChain {

    private final List<IHandler> handlerList;

    /**
     * HandlerChain 初始化的时候会收集所有实现了IHandler的类，那么HandlerChain 初始化是在什么时候？
     * 在Spring框架中，`HandlerChain` 的初始化通常发生在Spring应用的启动过程中。
     * 当Spring Boot应用启动时，Spring容器会进行以下几个步骤：
     * 1. 启动并初始化Spring上下文：这包括加载配置、创建和注册Bean定义等。
     * 2. 依赖注入：Spring容器会查找并自动装配依赖关系，比如通过 `@Autowired` 注解自动注入依赖的Bean。
     * 3. Bean的实例化：当你的 `HandlerChain` 类标有 `@Component` 注解时，
     *      Spring会在容器启动期间实例化它，并处理所有依赖的注入。
     * 4. 自动收集：`HandlerChain` 类会收集实现了 `IHandler` 接口的所有类。
     *      这通常是通过 Spring 的自动装配功能实现的，如通过构造函数注入所有 `IHandler` 类型的Bean。
     *
     * 因此，`HandlerChain` 的初始化和自动收集实现类的具体时机是在Spring应用启动的一部分，
     * 具体是在Spring容器初始化所有Bean后，且依赖注入完成时。
     * 这使得在应用程序的其他部分（例如控制器中）使用 `HandlerChain` 时，它已经被完全配置并准备好处理请求。
     *
     * @param handlerList
     */
    public HandlerChain(List<IHandler> handlerList) {
        this.handlerList = handlerList;
    }

    public void handle(String request, StringBuilder response) {
        handlerList.forEach(handler -> handler.handleRequest(request, response));
    }
}

