package io.github.dunwu.javacore.bio.chars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 使用 {@link BufferedReader} 从控制台读取输入的示例。
 * <p>
 * 功能：
 * - 提示用户输入内容。
 * - 如果用户输入 "exit"，程序退出。
 * - 否则，打印用户输入的内容。
 * </p>
 *
 * @author Zhang Peng
 */
public class BufferedReaderDemo {

    public static void main(String[] args) {
        System.out.println("欢迎使用输入演示程序。输入 'exit' 退出程序。");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            handleUserInput(reader);
        } catch (IOException e) {
            System.err.println("读取输入时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 处理用户输入。
     *
     * @param reader 用于读取用户输入的 BufferedReader
     * @throws IOException 如果读取失败
     */
    private static void handleUserInput(BufferedReader reader) throws IOException {
        while (true) {
            System.out.print("请输入内容：");
            String input = reader.readLine();
            if (input == null || "exit".equalsIgnoreCase(input.trim())) {
                System.out.println("程序退出，感谢使用！");
                break;
            }
            System.out.println("输入的内容为：" + input);
        }
    }
}
