package io.github.dunwu.javacore.bio;

import java.io.*;
import java.net.Socket;

// 客户端
class BioClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;

        try (Socket socket = new Socket(host, port)) {
            System.out.println("[客户端] 已连接到服务端");

            // 发送数据
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("你好，服务端！");
            System.out.println("[客户端] 发送消息: 你好，服务端！");

            // 接收服务端的响应
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String response = reader.readLine();
            System.out.println("[客户端] 收到响应: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}