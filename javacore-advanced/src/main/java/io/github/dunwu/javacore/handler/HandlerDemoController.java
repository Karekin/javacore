package io.github.dunwu.javacore.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandlerDemoController {

    private final HandlerChain handlerChain;

    public HandlerDemoController(HandlerChain handlerChain) {
        this.handlerChain = handlerChain;
    }

    @GetMapping("/process")
    public String process(@RequestParam String input) {
        StringBuilder response = new StringBuilder();
        handlerChain.handle(input, response);
        return response.toString();
    }
}

