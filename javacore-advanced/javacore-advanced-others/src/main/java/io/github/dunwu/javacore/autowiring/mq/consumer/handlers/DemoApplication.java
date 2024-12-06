package io.github.dunwu.javacore.autowiring.mq.consumer.handlers;

/**
 *  【背景描述】
 *  kafka 消费者需要灵活选择处理器，避免硬编码，将框架代码和逻辑代码解耦
 *  【原型系统】
 *  实时血缘系统
 * 【如何运行？】
 * 这个简化的示例演示了如何在Spring框架中使用依赖注入来处理基于类型映射的消息处理。
 * 在真实的Kafka集成场景中，你还需要配置Kafka相关的依赖和属性，以及实际的消息监听方法。
 *
 * 为了验证自动识别和注册消息处理器的功能，您可以采取以下步骤：
 *
 * 1. 添加命令行运行器
 *      在Spring Boot的主类或任何配置类中，添加一个命令行运行器（AppRunner）来模拟消息接收并触发消息处理器。
 *      这将帮助您在没有实际的Kafka服务器的情况下验证逻辑。
 * 2. 编译并运行 DemoApplication 类，观察控制台输出。
 * 3. 可以看到结果，虽然 messageHandlerMap 没有显式的初始化，但仍然找到了具体的实现类
 *
 * src/
 * └── main/
 *     ├── java/
 *     │   └── com/
 *     │       └── example/
 *     │           └── demo/
 *     │               ├── DemoApplication.java
 *     │               ├── config/
 *     │               │   └── HandlerConfig.java
 *     │               ├── handlers/
 *     │               │   ├── BaseMessageHandler.java
 *     │               │   ├── SqlMessageHandler.java
 *     │               │   └── XmlMessageHandler.java
 *     │               ├── listener/
 *     │               │   └── KafkaMessageListener.java
 *     │               └── annotations/
 *     │                   └── SourceHandler.java
 *     └── resources/
 *         └── application.properties
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

