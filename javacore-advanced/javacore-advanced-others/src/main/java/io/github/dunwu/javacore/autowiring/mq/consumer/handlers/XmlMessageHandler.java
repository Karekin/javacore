package io.github.dunwu.javacore.autowiring.mq.consumer.handlers;

@SourceHandler("XML")
public class XmlMessageHandler implements BaseMessageHandler {
    @Override
    public void handle() {
        System.out.println("Handling XML message.");
    }
}
