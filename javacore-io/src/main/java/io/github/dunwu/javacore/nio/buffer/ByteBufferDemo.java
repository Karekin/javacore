package io.github.dunwu.javacore.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 使用 {@link ByteBuffer} 演示直接缓冲区的操作。
 * <p>
 * 功能：
 * 1. 创建一个直接缓冲区。
 * 2. 将数据写入缓冲区。
 * 3. 从缓冲区读取数据并打印。
 * </p>
 *
 * @author
 */
public class ByteBufferDemo {

    public static void main(String[] args) {
        // 创建直接缓冲区
        int bufferSize = 10;
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

        // 设置要写入缓冲区的内容
        byte[] data = {1, 3, 5, 7, 9};
        writeToBuffer(buffer, data);

        // 从缓冲区读取内容并打印
        System.out.print("缓冲区中的内容：");
        readFromBuffer(buffer);
    }

    /**
     * 将数据写入缓冲区。
     *
     * @param buffer 要写入数据的缓冲区
     * @param data   写入的数据
     */
    private static void writeToBuffer(ByteBuffer buffer, byte[] data) {
        if (data.length > buffer.capacity()) {
            throw new IllegalArgumentException("数据大小超出缓冲区容量");
        }
        buffer.put(data);
        buffer.flip(); // 准备读取
    }

    /**
     * 从缓冲区读取数据并打印。
     *
     * @param buffer 要读取数据的缓冲区
     */
    private static void readFromBuffer(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + " ");
        }
        System.out.println(); // 换行
    }
}
