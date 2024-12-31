package io.github.dunwu.javacore.bio.bytes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip 压缩和解压示例。
 *
 * 功能：
 * 1. 压缩文件到 ZIP 包。
 * 2. 解压 ZIP 包到文件或目录。
 *
 * @author Zhang Peng
 */
public class ZipStreamDemo {

    public static void main(String[] args) {
        try {
            // 打印当前工作目录
            System.out.println("当前工作目录：" + System.getProperty("user.dir"));

            String filePath = "demo.txt";
            String zipFilePath = "demo.zip";
            String dirPath = "demoDir";
            String extractedDirPath = "extractedDir";

            // 自动创建示例文件和目录
            createSampleFiles(filePath, dirPath);

            // 示例调用
            compressFile(filePath, zipFilePath);
            decompressZip(zipFilePath, extractedDirPath);
        } catch (Exception e) {
            System.err.println("操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 自动创建示例文件和目录。
     *
     * @param filePath 文件路径
     * @param dirPath  目录路径
     * @throws IOException 如果文件或目录创建失败
     */
    private static void createSampleFiles(String filePath, String dirPath) throws IOException {
        // 创建文件
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("这是一个示例文件内容。\nHello World!");
                System.out.println("已创建文件：" + file.getAbsolutePath());
            }
        }

        // 创建目录
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("已创建目录：" + dir.getAbsolutePath());
                // 在目录中创建几个示例文件
                for (int i = 1; i <= 3; i++) {
                    File sampleFile = new File(dir, "file" + i + ".txt");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(sampleFile))) {
                        writer.write("示例文件 " + i + " 的内容。\n");
                    }
                }
            }
        }
    }

    /**
     * 压缩单个文件到 ZIP 文件。
     *
     * @param inputFile  输入文件路径
     * @param zipFile    输出 ZIP 文件路径
     * @throws IOException 如果发生 IO 错误
     */
    public static void compressFile(String inputFile, String zipFile) throws IOException {
        File file = new File(inputFile);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在：" + inputFile);
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            zos.putNextEntry(new ZipEntry(file.getName()));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, length);
            }
            System.out.println("文件已压缩到：" + zipFile);
        }
    }

    /**
     * 解压 ZIP 文件到指定目录。
     *
     * @param zipFile    输入 ZIP 文件路径
     * @param outputDir  解压输出目录路径
     * @throws IOException 如果发生 IO 错误
     */
    public static void decompressZip(String zipFile, String outputDir) throws IOException {
        File outDir = new File(outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try (
                ZipFile zip = new ZipFile(zipFile);
                ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(zipFile)))
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(outputDir, entry.getName());
                if (!outFile.toPath().normalize().startsWith(outDir.toPath().normalize())) {
                    throw new IOException("ZIP 文件包含不安全的路径：" + entry.getName());
                }

                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    try (InputStream input = zip.getInputStream(entry);
                         FileOutputStream output = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = input.read(buffer)) != -1) {
                            output.write(buffer, 0, length);
                        }
                    }
                }
                System.out.println("已解压文件：" + outFile.getAbsolutePath());
            }
        }
    }
}
