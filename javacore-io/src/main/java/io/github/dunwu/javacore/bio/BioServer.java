package io.github.dunwu.javacore.bio;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    // 服务端
    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[服务端] 已启动，正在监听端口: " + port);

            while (true) {
                // 阻塞等待客户端连接
                System.out.println("[服务端] 等待客户端连接...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("[服务端] 接收到客户端连接: " + clientSocket.getInetAddress());

                // 处理客户端请求
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            System.out.println("[服务端] 准备接收数据...");

            // 阻塞等待数据到达
            String message = reader.readLine();
            System.out.println("[服务端] 收到消息: " + message);

            // 返回响应
            writer.println("[服务端] 已收到: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


