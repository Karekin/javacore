package io.github.dunwu.javacore.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

// 客户端代码
class NioClient {
    public static void main(String[] args) {
        try {
            // 创建SocketChannel
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            // 连接服务端
            if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080))) {
                while (!socketChannel.finishConnect()) {
                    System.out.println("[客户端] 正在连接服务端...");
                }
            }
            System.out.println("[客户端] 已连接到服务端");

            // 发送数据到服务端
            String message = "你好，服务端！\n";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            socketChannel.write(buffer);
            System.out.println("[客户端] 发送消息: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}