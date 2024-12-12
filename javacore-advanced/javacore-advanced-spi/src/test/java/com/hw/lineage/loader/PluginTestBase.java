package com.hw.lineage.loader;

import com.hw.lineage.loader.utils.Preconditions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: 此抽象类为与插件机制相关的单元测试提供基础支持。
 *               包含测试中常用到的资源访问和辅助方法。
 * @author: HamaWhite
 */
public abstract class PluginTestBase extends TestLogger {

    // 定义插件名称常量，用于在测试中方便引用
    public static final String PLUGIN_A = "plugin-a";
    public static final String PLUGIN_B = "plugin-b";

    // 定义插件对应的 JAR 文件名称，以 .jar 为后缀
    public static final String PLUGIN_A_JAR = PLUGIN_A + ".jar";
    public static final String PLUGIN_B_JAR = PLUGIN_B + ".jar";

    // 定义一个父类加载器，一般用于在插件测试中作为父加载器传入
    public static final ClassLoader PARENT_CLASS_LOADER = PluginTestBase.class.getClassLoader();

    /**
     * 根据给定的文件名字符串，创建并返回一个指向该 JAR 文件的 URL。
     * 若文件不存在，则会尝试在指定目录中寻找。
     *
     * @param fileString JAR 文件名字符串（如 "plugin-a.jar"）
     * @return 指向该 JAR 文件的 URL 对象
     * @throws MalformedURLException 当文件路径无法转换为合法 URL 时抛出
     */
    public URL createPluginJarURLFromString(String fileString) throws MalformedURLException {
        File file = locateJarFile(fileString);
        return file.toURI().toURL();
    }

    /**
     * 尝试在多种可能的路径下定位指定的 JAR 文件。
     * 优先顺序：
     * 1. 当前工作目录下
     * 2. target 目录下 (maven test 情形)
     * 3. javacore-advanced-spi/target 目录下 (idea test 情形)
     *
     * 如果在上述路径均未找到对应文件，则抛出异常。
     *
     * @param fileString 要定位的文件名字符串
     * @return 定位到的文件对象
     */
    public static File locateJarFile(String fileString) {
        File file = new File(fileString);

        // 若当前目录下未找到，则尝试在 target 下寻找
        if (!file.exists()) {
            file = new File("target/" + fileString);
        }

        // 若仍未找到，则尝试在 javacore-advanced-spi/target 下寻找
        if (!file.exists()) {
            file = new File("javacore-advanced-spi/target/" + fileString);
        }

        // 若还未找到，则抛出异常
        Preconditions.checkState(file.exists(), "Unable to locate jar file for test: " + fileString);

        return file;
    }
}
