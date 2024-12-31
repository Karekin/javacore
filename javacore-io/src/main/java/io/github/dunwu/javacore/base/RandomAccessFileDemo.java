package io.github.dunwu.javacore.base;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * {@link RandomAccessFile} 读写操作示例。
 * <p>
 * 注：{@link RandomAccessFile} 读写文件操作较为麻烦，更建议使用字节流或字符流方法。
 * </p>
 *
 * 本示例演示了如何写入和读取临时文件。
 */
public class RandomAccessFileDemo {

    private static final String FILE_NAME = "temp.log";

    public static void main(String[] args) throws IOException {
        // 写入数据
        writeData();

        // 读取数据
        readData();
    }

    /**
     * 使用 RandomAccessFile 写入数据到文件。
     */
    public static void writeData() throws IOException {
        File file = new File(FILE_NAME);
        try (RandomAccessFile rdf = new RandomAccessFile(file, "rw")) {
            // 写入第一组记录
            writeRecord(rdf, "zhangsan", 30);

            // 写入第二组记录
            writeRecord(rdf, "lisi    ", 31);

            // 写入第三组记录
            writeRecord(rdf, "wangwu  ", 32);

            System.out.println("数据写入完成，文件路径: " + file.getAbsolutePath());
        }
    }

    /**
     * 使用 RandomAccessFile 读取文件中的数据。
     */
    public static void readData() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("文件不存在，请先执行写入操作。");
            return;
        }

        try (RandomAccessFile rdf = new RandomAccessFile(file, "r")) {
            byte[] buffer = new byte[8];

            // 读取第二组记录
            rdf.skipBytes(12); // 跳过第一组记录
            readRecord(rdf, buffer, "第二个人的信息");

            // 读取第一组记录
            rdf.seek(0); // 回到文件开头
            readRecord(rdf, buffer, "第一个人的信息");

            // 读取第三组记录
            rdf.skipBytes(12); // 跳过第二组记录
            readRecord(rdf, buffer, "第三个人的信息");
        }
    }

    /**
     * 写入单个记录到 RandomAccessFile。
     *
     * @param rdf  RandomAccessFile 对象
     * @param name 姓名
     * @param age  年龄
     * @throws IOException 如果写入失败
     */
    private static void writeRecord(RandomAccessFile rdf, String name, int age) throws IOException {
        rdf.writeBytes(name); // 写入姓名
        rdf.writeInt(age);    // 写入年龄
    }

    /**
     * 读取单个记录并打印。
     *
     * @param rdf     RandomAccessFile 对象
     * @param buffer  用于存储读取的姓名字节
     * @param message 信息前缀
     * @throws IOException 如果读取失败
     */
    private static void readRecord(RandomAccessFile rdf, byte[] buffer, String message) throws IOException {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = rdf.readByte(); // 逐字节读取姓名
        }
        String name = new String(buffer); // 转换为字符串
        int age = rdf.readInt();         // 读取年龄
        System.out.println(message + " --> 姓名：" + name.trim() + "；年龄：" + age);
    }
}
