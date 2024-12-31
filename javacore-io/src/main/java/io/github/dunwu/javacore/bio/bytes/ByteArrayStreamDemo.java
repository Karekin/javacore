package io.github.dunwu.javacore.bio.bytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 内存操作流示例。
 * <p>
 * 演示通过字节数组流将大写字符串转换为小写字符串。
 * </p>
 *
 */
public class ByteArrayStreamDemo {

    public static void main(String[] args) throws IOException {
        String inputString = "HELLOWORLD"; // 原始字符串
        System.out.println("原始内容: " + inputString);

        // 转换字符串
        String outputString = convertToLowerCase(inputString);
        System.out.println("转换后的内容: " + outputString);
    }

    /**
     * 将字符串中的大写字母转换为小写字母。
     *
     * @param input 原始字符串
     * @return 转换后的字符串
     */
    private static String convertToLowerCase(String input) throws IOException {
        // 初始化字节输入流和字节输出流
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int byteData;
            while ((byteData = inputStream.read()) != -1) {
                char character = (char) byteData; // 将字节转换为字符
                outputStream.write(Character.toLowerCase(character)); // 转换为小写并写入输出流
            }

            return outputStream.toString(); // 输出流中的数据转换为字符串
        }
    }
}
