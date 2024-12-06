package io.github.dunwu.javacore.autowiring.lineage.handlers.chain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码中两种依赖注入方式的功能是等价的，但它们在使用和设计上有一些细微的差别，需要根据具体的情况选择适合的方式。

 * 【写法一：构造器注入】
 * 一、优点
 * 1. 强制依赖：
 *    - 构造器注入会在对象实例化时强制注入依赖，确保依赖的不可或缺性（handlerChain 必须有值，不能为 null）。
 *    - 因为字段是 final 的，这种方式可以避免不小心覆盖依赖。

 * 2. 便于单元测试：
 *    - 在单元测试中，容易通过构造函数注入模拟的依赖对象（如 Mock 对象）。

 * 3. 线程安全性：
 *    - 因为字段是 final 的，一旦初始化，handlerChain 的引用不会被修改，线程安全。

 * 4. 代码可读性：
 *    - 构造器注入使依赖一目了然，可以通过构造函数的参数看出类所需要的依赖。

 * 二、 缺点
 * - 如果依赖过多，构造器参数可能会变得冗长，影响代码的可维护性。

 * 【写法二：字段注入（使用 @Autowired）】

 * 一、优点
 * 1. 简洁：
 *    - 不需要显式声明构造函数，代码更简单，尤其是对于较小的类。

 * 2. 快速实现：
 *    - 在现有类中添加新的依赖更快，直接加字段并标注 @Autowired 即可，无需修改构造函数。

 * 二、缺点
 * 1. 不够显式：
 *    - 字段注入的依赖关系隐式存在，代码阅读时难以快速了解类需要哪些依赖。

 * 2. 不利于单元测试：
 *    - 在单元测试中，需要通过反射或容器手段注入模拟依赖（如 Mock 对象），增加了复杂性。

 * 3. 线程安全性较低：
 *    - 字段注入允许 handlerChain 在对象实例化后被修改，可能引发线程安全问题。

 * 4. 依赖可能为 null：
 *    - 如果 Spring 容器配置有误或未能正确扫描依赖，字段注入可能会导致 handlerChain 为 null，这在运行时才会被发现。

 * 三、等价性分析

 * 四、功能等价性
 * 两种方式最终实现的功能是等价的：Spring 容器会负责注入 HandlerChain 实例到 HandlerDemoController 中，代码的实际业务逻辑运行结果相同。

 * 五、使用场景上的差异
 * - 构造器注入更加适合需要明确依赖关系、不可变性要求强烈的场景。
 * - 字段注入更适合对代码简洁性要求高、测试需求较低的场景。

 * 【最佳实践】
 * 现代 Spring 应用开发中，更推荐使用构造器注入（即写法一），因为它提供了更高的可维护性和更强的类型安全性。
 * 字段注入（写法二）虽然可以用，但通常被视为一种简化手段，适合简单场景或临时代码。
 * 此外，Spring 官方也倾向于提倡构造器注入，因为它更契合面向对象的设计原则（如单一职责和依赖倒置）。
 */
@RestController
public class HandlerDemoController {
    // 依赖注入，写法一：
    private final HandlerChain handlerChain;

    public HandlerDemoController(HandlerChain handlerChain) {
        this.handlerChain = handlerChain;
    }
    // 依赖注入，写法二：
//    @Autowired
//    private HandlerChain handlerChain;

    @GetMapping("/process")
    public String process(@RequestParam String input) {
        StringBuilder response = new StringBuilder();
        handlerChain.handle(input, response);
        return response.toString();
    }
}

