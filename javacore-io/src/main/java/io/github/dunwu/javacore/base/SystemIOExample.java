package io.github.dunwu.javacore.base;

import java.io.*;

/**
 * System 输入/输出/错误流示例
 *
 * 示例演示了 System.in、System.out 和 System.err 的使用和重定向。
 *
 */
public class SystemIOExample {

    public static void main(String[] args) throws Exception {
        // 示例 1: System.err 重定向
        redirectSystemErr();

        // 示例 2: System.in 示例
        readFromSystemIn();

        // 示例 3: System.out 重定向
        redirectSystemOut();
    }

    /**
     * 示例 1: System.err 重定向到内存中的 OutputStream。
     */
    private static void redirectSystemErr() throws IOException {
        System.out.println("[示例 1: System.err 重定向]");
        OutputStream bos = new ByteArrayOutputStream(); // 实例化
        PrintStream ps = new PrintStream(bos); // 实例化
        System.setErr(ps); // 输出重定向
        System.err.print("此处有误");
        System.out.println("重定向后的 System.err 内容: " + bos.toString()); // 输出内存中的数据
    }

    /**
     * 示例 2: 从 System.in 读取用户输入。
     */
    private static void readFromSystemIn() throws IOException {
        System.out.println("[示例 2: 从 System.in 读取输入]");
        InputStream input = System.in;
        StringBuffer buf = new StringBuffer();
        System.out.print("请输入内容：");
        int temp;
        while ((temp = input.read()) != -1) {
            char c = (char) temp;
            if (c == '\n') {
                break;
            }
            buf.append(c);
        }
        System.out.println("输入的内容为：" + buf);
    }

    /**
     * 示例 3: 重定向 System.out 输出到文件。
     */
    private static void redirectSystemOut() throws Exception {
        System.out.println("[示例 3: System.out 重定向到文件]");
        OutputStream out = new FileOutputStream("d:\\test.txt");
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);
        System.out.println("人生若只如初见，何事秋风悲画扇");
        ps.close();
        out.close();
        System.out.println("输出已重定向到文件 d:\\test.txt");
    }
}
