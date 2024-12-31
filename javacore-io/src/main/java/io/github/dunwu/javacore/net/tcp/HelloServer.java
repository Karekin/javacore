package io.github.dunwu.javacore.net.tcp;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple TCP server that sends a "hello world" message to a client.
 */
public class HelloServer {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        System.out.println("服务器运行中，等待客户端连接...");

        try (ServerSocket server = new ServerSocket(PORT)) {
            // 等待客户端连接（阻塞操作）
            try (Socket client = server.accept();
                 PrintStream out = new PrintStream(client.getOutputStream())) {

                // 向客户端发送信息
                out.println("hello world");
                System.out.println("服务器已向客户端发送消息。");
            } catch (IOException e) {
                System.err.println("处理客户端连接时发生错误: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("服务器启动失败: " + e.getMessage());
        }

        System.out.println("服务器已关闭。");
    }
}
