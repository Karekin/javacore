package io.github.dunwu.javacore.bio.bytes;

import java.io.*;
import java.nio.file.Files;

/**
 * 文件输入输出流示例。
 * <p>
 * 演示通过字节流实现文件的写入和读取操作。
 * </p>
 *
 */
public class FileStreamDemo {

    private static final String FILEPATH = "temp.log";

    public static void main(String[] args) {
        try {
            write(FILEPATH);
            read(FILEPATH);
        } catch (IOException e) {
            System.err.println("文件操作出现错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将内容写入文件。
     *
     * @param filepath 文件路径
     * @throws IOException 如果写入失败
     */
    public static void write(String filepath) throws IOException {
        File file = new File(filepath);

        // 确保父目录存在
        if (file.getParentFile() != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("无法创建父目录: " + file.getParent());
        }

        // 使用 try-with-resources 确保流被正确关闭
        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            String content = "Hello World\n";
            byte[] bytes = content.getBytes();
            out.write(bytes);
            System.out.println("成功写入文件: " + filepath);
        }
    }

    /**
     * 从文件中读取内容。
     *
     * @param filepath 文件路径
     * @throws IOException 如果读取失败
     */
    public static void read(String filepath) throws IOException {
        File file = new File(filepath);

        // 检查文件是否存在
        if (!file.exists()) {
            throw new FileNotFoundException("文件未找到: " + filepath);
        }

        // 使用 try-with-resources 确保流被正确关闭
        try (InputStream input = Files.newInputStream(file.toPath())) {
            byte[] bytes = new byte[(int) file.length()];
            int length = input.read(bytes);

            System.out.println("读入数据的长度: " + length);
            System.out.println("内容为:\n" + new String(bytes));
        }
    }
}
