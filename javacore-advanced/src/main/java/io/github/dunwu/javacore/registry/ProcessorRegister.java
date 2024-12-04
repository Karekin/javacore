package io.github.dunwu.javacore.registry;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Processor 注册器
 * 根据 SQL 类型动态分发到对应处理器
 */
public class ProcessorRegister {

    private static final Map<Type, Object> PROCESSOR_MAP = new HashMap<>();

    private ProcessorRegister() {}

    public static void register(Class<?> clazz, Object processor) {
        PROCESSOR_MAP.put(clazz, processor);
    }

    public static Object getProcessor(Type clazz) {
        return PROCESSOR_MAP.get(clazz);
    }
}
