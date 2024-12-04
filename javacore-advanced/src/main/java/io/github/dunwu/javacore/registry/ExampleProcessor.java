package io.github.dunwu.javacore.registry;


/**
 * 示例处理器 - 处理类型为 ExampleType
 */
@ObjectType(clazz = ExampleType.class)
public class ExampleProcessor {
    public void process(Object input) {
        System.out.println("Processing input of type: " + input.getClass().getSimpleName());
    }
}
