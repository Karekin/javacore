package io.github.dunwu.javacore.nio.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel 示例：演示文件写入、读写复制和内存映射。
 *
 * 功能：
 * 1. 写入内容到文件。
 * 2. 使用通道复制文件。
 * 3. 使用内存映射读取文件内容。
 *
 * 用法：
 * - 写入文件：`main("write")`
 * - 复制文件：`main("copy")`
 * - 读取文件：`main("read")`
 *
 * @author
 */
public class FileChannelDemo {

    public static void main(String[] args) {
//        String mode = args.length > 0 ? args[0].toLowerCase() : "write";
        String mode = "write";
        try {
            switch (mode) {
                case "write":
                    writeToFile("out.txt");
                    break;
                case "copy":
                    copyFile("out.txt", "out_copy.txt");
                    break;
                case "read":
                    readFileUsingMappedBuffer("out.txt");
                    break;
                default:
                    System.out.println("未知模式：" + mode + "。可用模式：write, copy, read");
            }
        } catch (Exception e) {
            System.err.println("操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 使用 FileChannel 将内容写入文件。
     *
     * @param fileName 写入的文件名
     * @throws IOException 如果发生 IO 错误
     */
    private static void writeToFile(String fileName) throws IOException {
        String[] info = {"大风起兮云飞扬，", "威加海内兮归故乡，", "安得猛士兮守四方。"};
        File file = new File(fileName);

        // 确保父目录存在
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("无法创建父目录：" + file.getParent());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             FileChannel fc = fos.getChannel()) {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            for (String line : info) {
                buf.put(line.getBytes());
            }
            buf.flip();
            fc.write(buf);
            System.out.println("内容已写入文件：" + fileName);
        }
    }

    /**
     * 使用 FileChannel 复制文件。
     *
     * @param sourceFile 源文件路径
     * @param destFile   目标文件路径
     * @throws IOException 如果发生 IO 错误
     */
    private static void copyFile(String sourceFile, String destFile) throws IOException {
        File src = new File(sourceFile);
        File dest = new File(destFile);

        try (FileInputStream input = new FileInputStream(src);
             FileOutputStream output = new FileOutputStream(dest);
             FileChannel fin = input.getChannel();
             FileChannel fout = output.getChannel()) {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while (fin.read(buf) != -1) {
                buf.flip();
                fout.write(buf);
                buf.clear();
            }
            System.out.println("文件已复制到：" + destFile);
        }
    }

    /**
     * 使用内存映射读取文件内容。
     *
     * @param fileName 文件路径
     * @throws IOException 如果发生 IO 错误
     */
    private static void readFileUsingMappedBuffer(String fileName) throws IOException {
        File file = new File(fileName);

        try (FileInputStream input = new FileInputStream(file);
             FileChannel fin = input.getChannel()) {
            MappedByteBuffer mbb = fin.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            byte[] data = new byte[(int) file.length()];
            int index = 0;
            while (mbb.hasRemaining()) {
                data[index++] = mbb.get();
            }
            System.out.println("文件内容：\n" + new String(data));
        }
    }
}

