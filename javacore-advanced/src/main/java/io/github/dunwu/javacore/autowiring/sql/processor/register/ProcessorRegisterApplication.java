package io.github.dunwu.javacore.autowiring.sql.processor.register;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 【背景描述】
 *  SQL处理器数量很多，显式注册逻辑复杂，可以通过bean拦截器BeanPostProcessor，在服务启动时注册所有的处理器
 *
 * 一、自动注册处理器：
 * Spring 容器在初始化 ExampleProcessor 时，由 ProcessorRegistrationInterceptor 自动拦截。
 * 检查是否有 @ObjectType 注解，如果有则将其注册到 ProcessorRegister。

 * 二、动态调用处理器：
 * 在 CommandLineRunner 中，通过 ProcessorRegister 获取 ExampleProcessor。
 * 动态调用其 process 方法处理 ExampleType。

 * 三、无需手动注册：
 * 原本需要手动调用 ProcessorRegister.register 的逻辑，现在完全由 ProcessorRegistrationInterceptor 接管。
 */
@SpringBootApplication
public class ProcessorRegisterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorRegisterApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 模拟动态调用处理器
        ExampleType exampleType = new ExampleType();
        Object processor = ProcessorRegister.getProcessor(ExampleType.class);

        if (processor != null) {
            System.out.println("Processor found. Invoking process method...");
            ((ExampleProcessor) processor).process(exampleType);
        } else {
            System.out.println("No processor found for type: " + ExampleType.class.getSimpleName());
        }
    }
}
