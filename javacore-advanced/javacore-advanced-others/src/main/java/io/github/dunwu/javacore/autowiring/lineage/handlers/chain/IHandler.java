package io.github.dunwu.javacore.autowiring.lineage.handlers.chain;

public interface IHandler {
    void handleRequest(String request, StringBuilder response);
}

