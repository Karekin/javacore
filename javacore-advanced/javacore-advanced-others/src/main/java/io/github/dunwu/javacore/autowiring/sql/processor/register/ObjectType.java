package io.github.dunwu.javacore.autowiring.sql.processor.register;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * SQLObjectType - 用于标记 SQL 类型对应的处理器
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObjectType {
    Class<?> clazz(); // 绑定的 SQL 类型
}
