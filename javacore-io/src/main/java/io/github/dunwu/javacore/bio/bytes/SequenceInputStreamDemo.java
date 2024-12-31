package io.github.dunwu.javacore.bio.bytes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 合并流示例。
 * <p>
 * 使用 {@link SequenceInputStream} 将多个 {@link InputStream} 合并为一个流。
 * </p>
 *
 * 功能：将 temp1.log 和 temp2.log 的内容合并，并保存到 temp3.log。
 *
 * @author Zhang Peng
 */
public class SequenceInputStreamDemo {

    public static void main(String[] args) {
        try {
            // 自动创建文件
            createSampleFiles();

            // 合并文件
            String inputFile1 = "temp1.log";
            String inputFile2 = "temp2.log";
            String outputFile = "temp3.log";
            mergeFiles(inputFile1, inputFile2, outputFile);

            System.out.println("文件合并完成，保存到：" + outputFile);
        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 合并两个文件并将结果保存到输出文件。
     *
     * @param inputFile1 第一个输入文件路径
     * @param inputFile2 第二个输入文件路径
     * @param outputFile 输出文件路径
     * @throws IOException 如果文件操作失败
     */
    public static void mergeFiles(String inputFile1, String inputFile2, String outputFile) throws IOException {
        File file1 = new File(inputFile1);
        File file2 = new File(inputFile2);

        // 检查输入文件是否存在
        if (!file1.exists()) {
            throw new FileNotFoundException(inputFile1 + " 文件不存在");
        }
        if (!file2.exists()) {
            throw new FileNotFoundException(inputFile2 + " 文件不存在");
        }

        try (
                InputStream is1 = Files.newInputStream(file1.toPath());
                InputStream is2 = Files.newInputStream(file2.toPath());
                SequenceInputStream sis = new SequenceInputStream(is1, is2);
                OutputStream os = Files.newOutputStream(Paths.get(outputFile))
        ) {
            int data;
            while ((data = sis.read()) != -1) {
                os.write(data);
            }
        }
    }

    /**
     * 自动创建示例文件。
     *
     * @throws IOException 如果文件创建失败
     */
    private static void createSampleFiles() throws IOException {
        String[] files = { "temp1.log", "temp2.log" };
        String[] contents = { "文件1内容\n", "文件2内容\n" };

        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            if (!file.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(contents[i]);
                    System.out.println(files[i] + " 已创建");
                }
            }
        }
    }
}
