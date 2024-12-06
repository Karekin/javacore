package io.github.dunwu.javacore.autowiring.mq.consumer.handlers;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 对注解的行为感到困惑
 * 1.为什么给 Handler 标记了此注解，bean 名称就变成了注解中的参数，例如"SQL"
 *      gpt说是加了显性的配置 HandlerConfig，但是我注掉了还是一样的结果，和项目中的现象符合
 * 2.如果直接把 Handler 的注解去掉，那么不会注册到 messageHandlerMap中，因为不再试一个bean
 *      因为注解 SourceHandler本身继承了@Component，所以要想不依赖注解还能成功注册，需要加上 @Component
 *      此时可以看到，bean的名称就是默认的类名了。
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceHandler {
    String value();
}

