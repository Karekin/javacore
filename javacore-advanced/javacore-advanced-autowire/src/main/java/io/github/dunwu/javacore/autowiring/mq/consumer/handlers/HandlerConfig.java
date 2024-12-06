package io.github.dunwu.javacore.autowiring.mq.consumer.handlers;//package io.github.dunwu.javacore.autowiring.consumer.handlers;
//
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class HandlerConfig {
//
//    private final ConfigurableListableBeanFactory beanFactory;
//
//    public HandlerConfig(ConfigurableListableBeanFactory beanFactory) {
//        this.beanFactory = beanFactory;
//    }
//
//    @Bean
//    public Map<String, BaseMessageHandler> messageHandlerMap() {
//        String[] beanNames = beanFactory.getBeanNamesForType(BaseMessageHandler.class);
//        Map<String, BaseMessageHandler> handlers = new HashMap<>();
//        for (String beanName : beanNames) {
//            BaseMessageHandler handler = (BaseMessageHandler) beanFactory.getBean(beanName);
//            SourceHandler annotation = AnnotationUtils.findAnnotation(handler.getClass(), SourceHandler.class);
//            if (annotation != null) {
//                handlers.put(annotation.value(), handler);
//            }
//        }
//        return handlers;
//    }
//}
//
