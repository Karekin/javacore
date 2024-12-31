package io.github.dunwu.javacore.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 演示使用 {@link Charset} 进行字符编码和解码的示例。
 */
public class CharsetEnDeDemo {

    public static void main(String[] args) {
        // 使用支持中文的字符集（UTF-8）
        Charset charset = StandardCharsets.UTF_8;
        System.out.println("使用字符集：" + charset.displayName());

        try {
            // 获取编码器和解码器
            CharsetEncoder encoder = charset.newEncoder();
            CharsetDecoder decoder = charset.newDecoder();

            // 待处理的字符串
            String input = "梦里花落知多少";
            System.out.println("原始字符串：" + input);

            // 编码操作：将字符序列转换为字节序列
            CharBuffer charBuffer = CharBuffer.wrap(input);
            ByteBuffer byteBuffer = encoder.encode(charBuffer);
            System.out.println("编码后的字节数组：");
            printByteBuffer(byteBuffer);

            // 解码操作：将字节序列转换回字符序列
            byteBuffer.flip(); // 确保缓冲区从写模式切换到读模式
            CharBuffer decodedCharBuffer = decoder.decode(byteBuffer);
            System.out.println("解码后的字符串：" + decodedCharBuffer.toString());
        } catch (Exception e) {
            System.err.println("字符编码或解码失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印 ByteBuffer 中的字节内容。
     *
     * @param buffer 要打印的字节缓冲区
     */
    private static void printByteBuffer(ByteBuffer buffer) {
        buffer.rewind(); // 重置缓冲区以准备读取
        if (!buffer.hasRemaining()) {
            System.out.println("[空]");
            return;
        }
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            System.out.printf("0x%02X ", b);
        }
        System.out.println(); // 换行
    }
}
