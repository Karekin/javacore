package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

/**
 * A simple TCP client that connects to a server and reads a message.
 */
public class HelloClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        System.out.println("客户端启动，尝试连接到服务器...");

        try (Socket client = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            // 接收服务器消息
            String message = reader.readLine();
            System.out.println("客户端接收到服务器消息：" + message);
        } catch (IOException e) {
            System.err.println("连接服务器失败或读取数据失败: " + e.getMessage());
        }

        System.out.println("客户端退出。");
    }
}
