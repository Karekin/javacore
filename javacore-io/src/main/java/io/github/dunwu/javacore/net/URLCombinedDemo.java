package io.github.dunwu.javacore.net;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * A comprehensive demonstration of URL, URLConnection, and URL encoding/decoding.
 */
public class URLCombinedDemo {

    public static void main(String[] args) {
        try {
            demonstrateURL();
            demonstrateURLConnection();
            demonstrateEncoding();
        } catch (Exception e) {
            System.err.println("执行示例时发生错误: " + e.getMessage());
        }
    }

    private static void demonstrateURL() throws Exception {
        System.out.println("\n--- URL Demo ---");
        URL url = new URL("https", "www.baidu.com", 443, "/");
        try (InputStream input = url.openStream(); Scanner scan = new Scanner(input)) {
            scan.useDelimiter("\n"); // 设置读取分隔符
            while (scan.hasNext()) {
                System.out.println(scan.next());
            }
        }
    }

    private static void demonstrateURLConnection() throws Exception {
        System.out.println("\n--- URLConnection Demo ---");
        URL url = new URL("https://www.baidu.com");
        URLConnection urlCon = url.openConnection(); // 建立连接
        System.out.println("内容大小: " + urlCon.getContentLength());
        System.out.println("内容类型: " + urlCon.getContentType());
    }

    private static void demonstrateEncoding() throws Exception {
        System.out.println("\n--- URL Encoding/Decoding Demo ---");
        String keyWord = "乘风破浪会有时";
        String encoded = URLEncoder.encode(keyWord, "UTF-8"); // 进行编码的操作
        System.out.println("编码之后的内容: " + encoded);
        String decoded = URLDecoder.decode(encoded, "UTF-8"); // 进行解码操作
        System.out.println("解码之后的内容: " + decoded);
    }
}
