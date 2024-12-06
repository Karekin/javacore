package io.github.dunwu.javacore.autowiring.lineage.handlers.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2) // 优先级 2
public class SecondExampleHandler implements IHandler {

    @Override
    public void handleRequest(String request, StringBuilder response) {
        response.append("SecondExampleHandler processed: ").append(request).append("\n");
    }
}

