package io.github.dunwu.javacore.autowiring.sql.processor.register;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 注册后置处理器：自动拦截标注了 @SQLObjectType 的处理器，并将其动态注册到 ProcessorRegister
 *
 * ### 注解 @ObjectType 的原理
 *
 * @ObjectType 是一个自定义注解，结合 Spring 的 BeanPostProcessor 实现自动注册处理器的功能。
 *
 * #### 注解工作机制
 * 1. 定义注解：
 *    - 使用 @Target 指定了注解可以作用于“类”。
 *    - 使用 @Retention 指定了注解的保留策略为"RUNTIME"，使得注解在运行时可以通过反射访问。
 *    - 包含两个参数：
 *      - clazz：绑定的 SQL 类型。
 *      - parent：支持多个父类型（可选）。
 *
 * 2. Spring 组件扫描：
 *    - 注解上添加了 @Component，使得标注了该注解的类能够被 Spring 容器自动扫描和管理。
 *
 * 3. 注解处理器：
 *    - 使用 BeanPostProcessor 的 postProcessBeforeInitialization 方法，在 Spring 初始化每个 bean 前检查其是否被 @ObjectType 标注。
 *    - 如果注解存在，将注解的 clazz 与对应的处理器 bean 通过 ProcessorRegister 注册。
 *
 * ### 注册器 ProcessorRegister 的流程
 *
 * ProcessorRegister 是用于存储和检索各种 SQL 处理器的注册中心，其主要作用是通过 SQL 类型 动态分发到对应的处理器执行逻辑。
 *
 * #### 注册流程
 *
 * 1. 定义存储容器：
 *    - 使用 Map<Type, Object> 分别存储：
 *      - STATEMENT_PROCESSOR_MAP：SQL 语句处理器（如 SELECT、INSERT）。
 *      - SQL_SELECT_QUERY_PROCESSOR_MAP：查询语句处理器。
 *      - TABLE_SOURCE_PROCESSOR_MAP：表源处理器。
 *      - TABLE_SQL_EXPR_MAP：SQL 表达式处理器。
 *
 * 2. 注册逻辑：
 *    - 在 register 方法中，根据处理器类型 (StatementProcessor, SQLSelectQueryProcessor, 等) 将处理器实例存入对应的容器。
 *    - 处理器类的 clazz（即 SQL 类型）作为键，处理器实例作为值。
 *
 * 3. 处理器绑定：
 *    - 在 ProcessorRegistrationInterceptor 中：
 *      - 通过 BeanPostProcessor 拦截所有 Spring bean。
 *      - 检查 bean 的类是否包含 @ObjectType 注解。
 *      - 提取 clazz，调用 ProcessorRegister.register 方法注册到对应的容器中。
 *
 * #### 查询流程
 *
 * 1. 按类型获取处理器：
 *    - ProcessorRegister.getStatementProcessor 等方法从对应容器中检索处理器实例。
 *    - 如果找不到对应处理器，抛出 UnsupportedOperationException 异常。
 *
 * 2. 动态分发：
 *    - 通过 SQL AST（抽象语法树）节点的类型动态调用处理器：
 *      - 比如，SQLCaseExpr 类型会调用 SQLCaseExprProcessor。
 *    - 处理器通过递归方式进一步解析子节点，形成完整的语法树处理流程。
 *
 * ### 流程梳理
 *
 * 1. 处理器定义：
 *    - 开发者创建一个 SQL 处理器类，继承对应接口（如 SQLExprProcessor）。
 *    - 在类上添加 @ObjectType(clazz = XXX.class) 指定支持的 SQL 类型。
 *
 * 2. Spring 加载：
 *    - Spring 扫描到处理器类并创建实例。
 *    - ProcessorRegistrationInterceptor 拦截初始化过程，检查注解并完成注册。
 *
 * 3. 执行时动态分发：
 *    - 根据 SQL AST 节点类型调用 ProcessorRegister.getXxxProcessor。
 *    - 获取处理器后调用其 process 方法完成节点处理。
 *
 * ### 总结
 *
 * #### 注解原理
 * - @ObjectType 是通过 Spring 注解机制和运行时反射实现的类型绑定注解。
 * - 结合 BeanPostProcessor 动态注册处理器实例。
 *
 * #### 注册器原理
 * - ProcessorRegister 是一个类型到处理器实例的映射容器。
 * - 通过 SQL AST 的类型动态检索并调用对应的处理器。
 *
 * 这种设计有效解耦了 SQL 语法树的解析逻辑和具体处理逻辑，使得系统具有良好的扩展性和模块化。
 */
@Component
public class ProcessorRegistrationInterceptor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // 检查是否标注了 @SQLObjectType 注解
        ObjectType annotation = bean.getClass().getAnnotation(ObjectType.class);
        if (annotation != null) {
            // 注册到 ProcessorRegister
            ProcessorRegister.register(annotation.clazz(), bean);
            System.out.println("Registered processor for type: " + annotation.clazz().getSimpleName());
        }
        return bean;
    }
}
