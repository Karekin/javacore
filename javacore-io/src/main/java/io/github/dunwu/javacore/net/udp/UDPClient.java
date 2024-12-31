package io.github.dunwu.javacore.net.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * A simple UDP client that receives a message from a server.
 */
public class UDPClient {

    private static final int CLIENT_PORT = 9000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        System.out.println("UDP客户端启动，等待接收消息...");

        try (DatagramSocket socket = new DatagramSocket(CLIENT_PORT)) {
            // 创建用于接收数据的数据包
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // 接收数据
            socket.receive(packet);

            // 解析数据并输出
            String message = new String(packet.getData(), 0, packet.getLength());
            String senderInfo = "from " + packet.getAddress().getHostAddress() + ":" + packet.getPort();
            System.out.println("接收到的消息: " + message + " " + senderInfo);
        } catch (Exception e) {
            System.err.println("接收消息时发生错误: " + e.getMessage());
        }

        System.out.println("UDP客户端退出。");
    }
}
