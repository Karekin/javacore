package io.github.dunwu.javacore.jvm.fucking;

/**
 * 以一个无限递归的示例方法来展示栈溢出
 * <p>
 * 栈溢出时，Java 会抛出 StackOverflowError ，出现此种情况是因为方法运行的时候栈的大小超过了虚拟机的上限所致。
 * <p>
 * Java 应用程序唤起一个方法调用时就会在调用栈上分配一个栈帧，这个栈帧包含引用方法的参数，本地参数，以及方法的返回地址。
 * <p>
 * 这个返回地址是被引用的方法返回后,程序能够继续执行的执行点。
 * <p>
 * 如果没有一个新的栈帧所需空间，Java 就会抛出 StackOverflowError。
 * <p>
 * VM 参数：-Xss228k - 设置栈大小为 228k
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class StackOverflowErrorDemo {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        StackOverflowErrorDemo demo = new StackOverflowErrorDemo();
        try {
            demo.stackLeak();
        } catch (Throwable e) {
            System.out.println("栈深度：" + demo.stackLength);
            e.printStackTrace();
        }
    }

}
