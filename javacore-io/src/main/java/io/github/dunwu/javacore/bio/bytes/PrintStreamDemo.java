package io.github.dunwu.javacore.bio.bytes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;

/**
 * PrintStream 示例。
 * <p>
 * 演示通过 {@link PrintStream} 将格式化内容输出到文件。
 * </p>
 *
 * 功能：将文本写入指定文件。
 *
 * @author Zhang Peng
 */
public class PrintStreamDemo {

    public static void main(String[] args) {
        final String filepath = "test.txt";

        try {
            writeToFile(filepath);
            System.out.println("内容已成功写入文件：" + filepath);
        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将格式化内容写入指定文件。
     *
     * @param filepath 文件路径
     * @throws IOException 如果文件操作失败
     */
    private static void writeToFile(String filepath) throws IOException {
        File file = new File(filepath);

        // 确保父目录存在
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("无法创建父目录：" + file.getParent());
            }
        }

        try (OutputStream os = Files.newOutputStream(file.toPath());
             PrintStream ps = new PrintStream(os)) {

            ps.print("Hello ");
            ps.println("World!!!");
            ps.printf("姓名：%s；年龄：%d%n", "张三", 18);
        }
    }
}
