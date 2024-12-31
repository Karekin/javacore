package io.github.dunwu.javacore.net.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A simple UDP server that sends a message to a client.
 */
public class UDPServer {

    private static final int SERVER_PORT = 3000;
    private static final int CLIENT_PORT = 9000;
    private static final String MESSAGE = "Hello World!!!";
    private static final String CLIENT_HOST = "localhost";

    public static void main(String[] args) {
        System.out.println("UDP服务器启动，准备发送消息...");

        try (DatagramSocket socket = new DatagramSocket(SERVER_PORT)) {
            // 创建数据包
            byte[] buffer = MESSAGE.getBytes();
            InetAddress clientAddress = InetAddress.getByName(CLIENT_HOST);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientAddress, CLIENT_PORT);

            // 发送消息
            System.out.println("发送消息: " + MESSAGE);
            socket.send(packet);
            System.out.println("消息发送完成。");
        } catch (Exception e) {
            System.err.println("发送消息时发生错误: " + e.getMessage());
        }

        System.out.println("UDP服务器退出。");
    }
}
