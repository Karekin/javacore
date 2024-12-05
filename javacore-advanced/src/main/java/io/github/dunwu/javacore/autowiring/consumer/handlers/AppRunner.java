package io.github.dunwu.javacore.autowiring.consumer.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    private KafkaMessageListener kafkaMessageListener;

    @Override
    public void run(String... args) throws Exception {
        // 模拟接收到的消息
        String simulatedMessageSql = "SQL:这是一条SQL消息";
        String simulatedMessageXml = "XML:这是一条XML消息";

        // 触发消息处理器
        kafkaMessageListener.handleMessage(simulatedMessageSql);
        kafkaMessageListener.handleMessage(simulatedMessageXml);
    }
}

