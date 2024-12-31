package io.github.dunwu.javacore.bio.chars;

import java.io.*;
import java.nio.file.Files;

/**
 * InputStream 与 Reader 以及 OutputStream 与 Writer 的示例。
 *
 * 功能：
 * 1. 将 OutputStream 转换为 Writer 并写入内容。
 * 2. 将 InputStream 转换为 Reader 并读取内容。
 *
 * @author Zhang Peng
 */
public class StreamReaderWriterDemo {

    private static final String FILE_PATH = "temp.log";

    public static void main(String[] args) {
        try {
            writeToFile(FILE_PATH, "Hello, World!!");
            String content = readFromFile(FILE_PATH);
            System.out.println("读取的文件内容：\n" + content);
        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将字符串内容写入文件，使用 OutputStream 转换为 Writer。
     *
     * @param filePath 文件路径
     * @param content  写入的内容
     * @throws IOException 如果写入失败
     */
    public static void writeToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);

        // 确保父目录存在
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("无法创建父目录：" + file.getParent());
            }
        }

        // 使用 try-with-resources 确保流自动关闭
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()))) {
            writer.write(content);
            System.out.println("内容已写入文件：" + filePath);
        }
    }

    /**
     * 从文件读取内容，使用 InputStream 转换为 Reader。
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException 如果读取失败
     */
    public static String readFromFile(String filePath) throws IOException {
        File file = new File(filePath);

        // 检查文件是否存在
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在：" + filePath);
        }

        // 使用 try-with-resources 确保流自动关闭
        StringBuilder content = new StringBuilder();
        try (Reader reader = new InputStreamReader(Files.newInputStream(file.toPath()))) {
            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                content.append(buffer, 0, length);
            }
        }

        return content.toString();
    }
}
