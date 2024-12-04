package io.github.dunwu.javacore.concurrent.current.features.finalcase;

/**
 * 描述: 构造函数的错误重排导致线程可能看到 final 变量的值会变
 *
 * FinalExample 类演示了 Java 内存模型（Java Memory Model, JMM）中的一个问题，
 * 即构造函数的指令重排可能导致其他线程看到一个不一致的对象状态。
 * 在构造函数中，`final` 变量 `x` 的初始化可能会被指令重排，从而导致线程看到的对象状态不一致。
 *
 * 这个问题涉及到 `final` 变量在构造函数中的初始化顺序，以及对象发布的时机。
 * 详细内容参考：http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html
 *
 * @author zed
 * @since 2019-06-13 1:38 PM
 */
public class FinalExample {

    // 定义一个 Global 类，用来存放 FinalExample 类的实例
    public static class Global {
        public static FinalExample example;  // Global 用于保存 FinalExample 的实例
    }

    public final int x;  // final 变量 x，在构造函数中初始化
    public int y;  // 非 final 变量 y，也在构造函数中初始化
    private static FinalExample f;  // 静态变量 f，用于存储 FinalExample 类的实例

    /**
     * 构造函数（错误的实现）：
     * 在构造函数中，`x` 和 `y` 初始化的顺序是正确的，但存在问题。
     * `final` 变量 `x` 被正确初始化，但由于对象的发布方式不当，线程可能会看到一个
     * "不完整" 的 `FinalExample` 对象。问题在于对象发布时的顺序问题。
     * 由于可能存在指令重排，`Global.example = this;` 可能在 `x` 和 `y` 完成初始化之前就执行。
     * 这样，其他线程可能会看到 `x` 和 `y` 未完全初始化的对象。
     */
    public FinalExample() { // 错误的构造函数（可能导致问题）
        x = 3;  // final 变量 x 的初始化
        y = 4;  // 非 final 变量 y 的初始化

        // 允许 this 对象被发布给 Global.example，可能导致对象状态未完全初始化
        Global.example = this;  // 错误的构造 - 允许 this 对象在初始化完成之前被发布
    }

    /**
     * writer 方法：创建 FinalExample 对象并赋值给 f。
     * 这个方法模拟了创建 FinalExample 对象的过程，并将其赋值给静态变量 f。
     */
    public static void writer() {
        f = new FinalExample();  // 创建 FinalExample 对象并赋值给 f
    }

    /**
     * reader 方法：从静态变量 f 中读取 x 和 y 的值。
     * 这个方法模拟了在其他线程中读取 FinalExample 对象字段的过程。
     * 由于构造函数中的指令重排问题，可能导致读取到未完全初始化的字段值。
     */
    public static void reader() {
        if (f != null) {
            // 读取 f 对象的字段 x 和 y
            int i = f.x;  // 读取 final 变量 x
            int j = f.y;  // 读取非 final 变量 y
            // TDOO 没复现出“逸出”导致的指令重排问题
            System.out.println("final 变量 x 的值为：" + i);
            System.out.println("final 变量 y 的值为：" + j);
        }
    }

    /**
     * main 方法：
     * 1. 调用 writer() 方法创建 FinalExample 对象并进行赋值。
     * 2. 输出 Global.example，验证对象发布的顺序和状态。
     * 3. 调用 reader() 方法，读取对象的字段。
     */
    public static void main(String[] args) {
        writer();  // 创建 FinalExample 对象
        System.out.println(Global.example);  // 输出 Global.example，查看对象状态
        reader();  // 调用 reader 方法，读取 FinalExample 对象的字段
    }
}
