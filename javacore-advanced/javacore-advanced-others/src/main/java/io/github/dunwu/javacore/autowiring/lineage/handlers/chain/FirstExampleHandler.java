package io.github.dunwu.javacore.autowiring.lineage.handlers.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // 优先级 1（越小优先级越高）
public class FirstExampleHandler implements IHandler {

    @Override
    public void handleRequest(String request, StringBuilder response) {
        response.append("FirstTableSupplementHandler processed: ").append(request).append("\n");
    }
}
