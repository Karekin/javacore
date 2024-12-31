package io.github.dunwu.javacore.nio.buffer;

import java.nio.*;

/**
 * IntBuffer 示例：演示基本操作、创建子缓冲区和创建只读缓冲区。
 *
 * 功能：
 * 1. 主缓冲区操作。
 * 2. 子缓冲区操作。
 * 3. 只读缓冲区操作。
 *
 * 用法：
 * 根据传入的参数执行不同的示例。
 *
 * - 主缓冲区操作：`main("main")`
 * - 子缓冲区操作：`main("sub")`
 * - 只读缓冲区操作：`main("readonly")`
 *
 */
public class IntBufferDemo {

    public static void main(String[] args) {
//        String mode = args.length > 0 ? args[0].toLowerCase() : "main";
        String mode = "main";

        switch (mode) {
            case "main":
                mainBufferDemo();
                break;
            case "sub":
                subBufferDemo();
                break;
            case "readonly":
                readOnlyBufferDemo();
                break;
            default:
                System.out.println("未知模式：" + mode + "。可用模式：main, sub, readonly");
        }
    }

    /**
     * 主缓冲区示例。
     */
    private static void mainBufferDemo() {
        IntBuffer buf = IntBuffer.allocate(10); // 创建一个大小为10的缓冲区
        System.out.print("1、写入数据之前的position、limit和capacity：");
        printBufferProperties(buf);

        int[] temp = {5, 7, 9}; // 定义一个int数组
        buf.put(3); // 添加单个数据
        buf.put(temp); // 添加多个数据
        System.out.print("2、写入数据之后的position、limit和capacity：");
        printBufferProperties(buf);

        buf.flip(); // 重设缓冲区
        System.out.print("3、准备输出数据时的position、limit和capacity：");
        printBufferProperties(buf);
        System.out.print("缓冲区中的内容：");
        printBufferContent(buf);
    }

    /**
     * 子缓冲区示例。
     */
    private static void subBufferDemo() {
        System.out.println("=== 子缓冲区示例开始 ===");

        // 创建一个大小为10的缓冲区
        IntBuffer buf = IntBuffer.allocate(10);
        System.out.println("1. 初始化主缓冲区：");
        printBufferProperties(buf);

        // 填充缓冲区内容
        for (int i = 0; i < 10; i++) {
            buf.put(2 * i + 1);
        }
        System.out.println("2. 填充主缓冲区内容（10个奇数）：");
        printBufferProperties(buf);
        buf.flip();
        System.out.print("主缓冲区内容：");
        printBufferContent(buf);

        // 创建子缓冲区
        buf.position(2);
        buf.limit(6);
        IntBuffer sub = buf.slice();
        System.out.println("3. 创建子缓冲区（主缓冲区位置从2到5）：");
        printBufferProperties(sub);

        // 修改子缓冲区内容
        System.out.println("4. 修改子缓冲区内容：");
        for (int i = 0; i < sub.capacity(); i++) {
            int temp = sub.get(i);
            sub.put(i, temp - 1); // 子缓冲区每个元素减1
            System.out.printf("   子缓冲区位置 %d 的值修改为 %d -> %d%n", i, temp, temp - 1);
        }

        // 重设主缓冲区
        buf.flip();
        buf.limit(buf.capacity());
        System.out.println("5. 重设主缓冲区后：");
        printBufferProperties(buf);

        // 打印主缓冲区内容
        System.out.print("主缓冲区中的内容：");
        printBufferContent(buf);

        System.out.println("=== 子缓冲区示例结束 ===");
    }

    /**
     * 只读缓冲区示例。
     */
    private static void readOnlyBufferDemo() {
        IntBuffer buf = IntBuffer.allocate(10); // 创建一个大小为10的缓冲区
        for (int i = 0; i < 10; i++) {
            buf.put(2 * i + 1); // 填充缓冲区内容
        }

        IntBuffer read = buf.asReadOnlyBuffer(); // 创建只读缓冲区
        read.flip(); // 重设缓冲区
        System.out.print("只读缓冲区的内容：");
        printBufferContent(read);

        try {
            read.put(30); // 尝试修改只读缓冲区，预期会抛出异常
        } catch (ReadOnlyBufferException e) {
            System.out.println("\n尝试修改只读缓冲区时发生错误：" + e.getMessage());
        }
    }

    /**
     * 打印缓冲区的属性。
     *
     * @param buffer 要打印的缓冲区
     */
    private static void printBufferProperties(IntBuffer buffer) {
        System.out.println("position = " + buffer.position() + "，limit = " + buffer.limit() + "，capacity = " + buffer.capacity());
    }

    /**
     * 打印缓冲区的内容。
     *
     * @param buffer 要打印的缓冲区
     */
    private static void printBufferContent(IntBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + "、");
        }
        System.out.println(); // 换行
    }
}
