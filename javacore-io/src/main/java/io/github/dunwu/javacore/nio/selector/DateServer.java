package io.github.dunwu.javacore.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于 NIO 的日期服务器，支持多个端口监听。
 *
 * 验证方法：启动服务器后，在终端中使用 telnet 测试：telnet localhost 8000
 */
public class DateServer {

    private static final int[] PORTS = {8000, 8001, 8002, 8003, 8005, 8006};

    public static void main(String[] args) {
        try (Selector selector = Selector.open()) {
            // 初始化多个服务器通道
            for (int port : PORTS) {
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.socket().bind(new InetSocketAddress(port));
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("服务器运行，在 " + port + " 端口监听。");
            }

            // 轮询处理事件
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        }
                    } catch (IOException e) {
                        System.err.println("处理客户端连接时出错：" + e.getMessage());
                        e.printStackTrace();
                        key.cancel(); // 取消出错的 key
                    } finally {
                        iterator.remove(); // 移除已处理的 key
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("服务器运行时出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 处理新客户端的连接。
     *
     * @param key 表示一个客户端连接的 SelectionKey
     * @throws IOException 如果发生 IO 错误
     */
    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        if (clientChannel != null) {
            String clientAddress = null;
            try {
                clientAddress = String.valueOf(clientChannel.getRemoteAddress());
            } catch (IOException e) {
                System.err.println("获取客户端地址失败：" + e.getMessage());
            }

            System.out.println("接受来自客户端的连接：" + (clientAddress != null ? clientAddress : "未知地址"));
            clientChannel.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            String response = "当前的时间为：" + new Date();
            buffer.put(response.getBytes());
            buffer.flip();
            clientChannel.write(buffer);
            System.out.println("已发送响应：" + response);

            try {
                clientChannel.close();
                System.out.println("关闭客户端连接：" + (clientAddress != null ? clientAddress : "未知地址"));
            } catch (IOException e) {
                System.err.println("关闭客户端连接失败：" + e.getMessage());
            }
        }
    }
}
