package io.github.dunwu.javacore.net;

import java.net.InetAddress;

/**
 * A demonstration of using the InetAddress class to retrieve and display IP addresses.
 */
public class InetAddressDemo {

    public static void main(String[] args) {
        try {
            // 获取本机地址
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("本机的IP地址：" + localAddress.getHostAddress());

            // 获取远程地址
            InetAddress remoteAddress = InetAddress.getByName("www.baidu.com");
            System.out.println("www.baidu.com 的IP地址：" + remoteAddress.getHostAddress());

            // 检查本机是否可达
            boolean isReachable = localAddress.isReachable(5000);
            System.out.println("本机是否可达：" + isReachable);
        } catch (Exception e) {
            System.err.println("获取IP地址或检测可达性时发生错误: " + e.getMessage());
        }
    }
}
