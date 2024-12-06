package io.github.dunwu.javacore.autowiring.lineage.handlers.chain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 【背景描述】
 * 血缘系统的解析器有多个，采用责任链模式，避免显式的对链进行初始化，而是自动收集实现了IHandler接口的所有实现类
 * 【原型系统】
 * 实时血缘系统
 * 【验证流程】
 * 启动项目，在浏览器中输入：http://localhost:8080/process?input=test_input，可以看到输出：
 * FirstTableSupplementHandler processed: test_input SecondExampleHandler processed: test_input
 */
@SpringBootApplication
public class HandlerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HandlerApplication.class, args);
    }
}
