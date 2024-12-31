package io.github.dunwu.javacore.bio.chars;

import java.io.*;

/**
 * Reader 和 Writer 示例。
 * <p>
 * 演示通过 {@link FileWriter} 和 {@link FileReader} 进行文件读写操作。
 * </p>
 *
 * 功能：
 * 1. 写入指定内容到文件。
 * 2. 读取文件内容并输出。
 *
 * @author Zhang Peng
 */
public class FileReadWriteDemo {

    private static final String FILEPATH = "temp.log";

    public static void main(String[] args) {
        try {
            write(FILEPATH, "Hello World!!!\r\n");
            String content = read(FILEPATH);
            System.out.println("读取的文件内容为：\n" + content);
        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将指定内容写入文件。
     *
     * @param filepath 文件路径
     * @param content  要写入的内容
     * @throws IOException 如果写入失败
     */
    public static void write(String filepath, String content) throws IOException {
        File file = new File(filepath);

        // 确保父目录存在
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("无法创建父目录：" + file.getParent());
            }
        }

        // 使用 try-with-resources 确保流自动关闭
        try (Writer writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("内容已写入文件：" + filepath);
        }
    }

    /**
     * 从指定文件读取内容。
     *
     * @param filepath 文件路径
     * @return 读取的内容
     * @throws IOException 如果读取失败
     */
    public static String read(String filepath) throws IOException {
        File file = new File(filepath);

        // 检查文件是否存在
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在：" + filepath);
        }

        // 使用 try-with-resources 确保流自动关闭
        StringBuilder content = new StringBuilder();
        try (Reader reader = new FileReader(file)) {
            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                content.append(buffer, 0, length);
            }
        }

        return content.toString();
    }
}
