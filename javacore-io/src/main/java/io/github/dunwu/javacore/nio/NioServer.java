package io.github.dunwu.javacore.nio;
// NIO 示例代码，模拟非阻塞IO过程

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {

    public static void main(String[] args) {
        try {
            // 创建ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // 配置为非阻塞模式
            serverSocketChannel.bind(new InetSocketAddress(8080));
            System.out.println("[服务端] 启动，监听端口: 8080");

            // 创建Selector
            Selector selector = Selector.open();

            // 注册ServerSocketChannel到Selector，监听ACCEPT事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 阻塞等待就绪事件
                if (selector.select() == 0) {
                    continue;
                }

                // 获取就绪的事件集合
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove(); // 移除已处理的事件

                    if (key.isAcceptable()) {
                        // 处理客户端连接
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        // 处理客户端数据读取
                        handleRead(key);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false); // 设置为非阻塞模式
        System.out.println("[服务端] 接收到客户端连接: " + socketChannel.getRemoteAddress());

        // 注册SocketChannel到Selector，监听READ事件
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        int bytesRead = socketChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            System.out.println("[服务端] 收到消息: " + new String(data));
            buffer.clear();
        } else if (bytesRead == -1) {
            System.out.println("[服务端] 客户端断开连接: " + socketChannel.getRemoteAddress());
            socketChannel.close();
        }
    }
}

