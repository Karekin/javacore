package io.github.dunwu.javacore.autowiring.consumer.handlers;


import org.springframework.stereotype.Component;

//@SourceHandler("SQL")
@Component
public class SqlMessageHandler implements BaseMessageHandler {
    @Override
    public void handle() {
        System.out.println("Handling SQL message.");
    }
}
