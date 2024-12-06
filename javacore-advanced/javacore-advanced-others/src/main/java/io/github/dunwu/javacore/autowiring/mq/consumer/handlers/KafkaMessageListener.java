package io.github.dunwu.javacore.autowiring.mq.consumer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaMessageListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);
    /**
     * 【待验证】
     * 如果注入的是 Map 类型，key是bean名称（可能被注解的value方法覆盖），value是bean实例
     * 如果注入的是 List 类型，元素是bean实例
     * 如果注入的是 对象 类型，元素是接口的某一种实现，根据优先级选择
     */
    @Autowired
    private Map<String, BaseMessageHandler> messageHandlerMap;

    public void handleMessage(String message) {
        try {
            log.info("Received message: {}", message);
            // 假设message包含了消息类型信息，例如"SQL:...消息内容..."
            String[] parts = message.split(":", 2);
            String messageType = parts[0];
            String messageContent = parts.length > 1 ? parts[1] : "";

            BaseMessageHandler handler = messageHandlerMap.get(messageType);
            if (handler != null) {
                handler.handle();
                log.info("Handled by: {}", handler.getClass().getSimpleName());
            } else {
                log.warn("No handler found for message type: {}", messageType);
            }
        } catch (Exception e) {
            log.error("Error handling message: {}", message, e);
        }
    }
}
