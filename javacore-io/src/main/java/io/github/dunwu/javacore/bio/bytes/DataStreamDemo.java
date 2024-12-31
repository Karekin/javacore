package io.github.dunwu.javacore.bio.bytes;

import java.io.*;
import java.nio.file.Files;

/**
 * 数据输入输出流示例。
 * <p>
 * 演示通过 {@link DataInputStream} 和 {@link DataOutputStream} 实现数据的序列化与反序列化。
 * </p>
 *
 * @author Zhang Peng
 */
public class DataStreamDemo {

    public static final String FILEPATH = "temp.log";

    public static void main(String[] args) {
        try {
            write(FILEPATH);
            read(FILEPATH);
        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将商品数据写入文件。
     *
     * @param filepath 文件路径
     * @throws IOException 如果写入失败
     */
    private static void write(String filepath) throws IOException {
        File file = new File(filepath);

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
            String[] names = { "衬衣", "手套", "围巾" };
            float[] prices = { 98.3f, 30.3f, 50.5f };
            int[] nums = { 3, 2, 1 };

            for (int i = 0; i < names.length; i++) {
                dos.writeUTF(names[i]); // 使用 writeUTF 写入字符串
                dos.writeFloat(prices[i]);
                dos.writeInt(nums[i]);
            }
            System.out.println("数据写入完成：" + filepath);
        }
    }

    /**
     * 从文件中读取商品数据。
     *
     * @param filepath 文件路径
     * @throws IOException 如果读取失败
     */
    private static void read(String filepath) throws IOException {
        File file = new File(filepath);

        try (DataInputStream dis = new DataInputStream(Files.newInputStream(file.toPath()))) {
            System.out.println("读取的数据：");

            while (true) {
                try {
                    String name = dis.readUTF(); // 使用 readUTF 读取字符串
                    float price = dis.readFloat();
                    int num = dis.readInt();
                    System.out.printf("名称：%s；价格：%5.2f；数量：%d\n", name, price, num);
                } catch (EOFException e) {
                    // 捕获 EOFException 表示文件读取完毕
                    break;
                }
            }
        }
    }
}
