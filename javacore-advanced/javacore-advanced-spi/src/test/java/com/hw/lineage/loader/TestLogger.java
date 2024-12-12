package com.hw.lineage.loader;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 为测试用例添加自动化的测试名称日志记录功能。
 * 继承此类的测试类会自动记录当前正在执行的测试名称，并在测试失败时记录失败原因。
 *
 * @description: TestLogger
 * @author: HamaWhite
 */
public class TestLogger {

    // 使用 SLF4J 日志记录器，用于记录测试过程中的信息和错误
    protected final Logger log = LoggerFactory.getLogger(getClass());

    // JUnit 的规则（Rule），允许定义在每个测试方法运行前后自动执行的行为
    @Rule
    public TestRule watchman =
            new TestWatcher() {

                /**
                 * 在测试开始时执行，用于记录测试名称和运行状态。
                 * @param description 测试用例的描述信息，包括测试类名和方法名。
                 */
                @Override
                public void starting(Description description) {
                    log.info(
                            "\n================================================================================"
                                    + "\nTest {} is running." // 正在运行的测试
                                    + "\n--------------------------------------------------------------------------------",
                            description);
                }

                /**
                 * 当测试成功完成时执行，用于记录测试的成功状态。
                 * @param description 测试用例的描述信息。
                 */
                @Override
                public void succeeded(Description description) {
                    log.info(
                            "\n--------------------------------------------------------------------------------"
                                    + "\nTest {} successfully run." // 测试成功运行
                                    + "\n================================================================================",
                            description);
                }

                /**
                 * 当测试失败时执行，用于记录测试失败的信息和堆栈跟踪。
                 * @param e 测试失败时抛出的异常。
                 * @param description 测试用例的描述信息。
                 */
                @Override
                public void failed(Throwable e, Description description) {
                    log.error(
                            "\n--------------------------------------------------------------------------------"
                                    + "\nTest {} failed with:\n{}" // 测试失败的详细堆栈信息
                                    + "\n================================================================================",
                            description,
                            exceptionToString(e));
                }
            };

    /**
     * 将异常的堆栈跟踪转换为字符串表示形式。
     *
     * @param t 要转换的异常对象。
     * @return 异常的堆栈跟踪字符串。如果异常为 null，则返回 "(null)"。
     */
    private static String exceptionToString(Throwable t) {
        if (t == null) {
            return "(null)";
        }

        try {
            // 使用 StringWriter 和 PrintWriter 将异常的堆栈信息写入字符串中
            StringWriter stm = new StringWriter();
            PrintWriter wrt = new PrintWriter(stm);
            t.printStackTrace(wrt);
            wrt.close();
            return stm.toString();
        } catch (Throwable ignored) {
            // 如果在生成堆栈跟踪过程中发生错误，返回异常类名和错误信息
            return t.getClass().getName() + " (error while printing stack trace)";
        }
    }
}
