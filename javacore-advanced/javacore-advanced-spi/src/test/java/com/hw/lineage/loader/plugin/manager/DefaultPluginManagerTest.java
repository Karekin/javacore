package com.hw.lineage.loader.plugin.manager;

import com.google.common.collect.Lists;
import com.hw.lineage.loader.PluginTestBase;
import com.hw.lineage.loader.plugin.PluginDescriptor;
import com.hw.lineage.loader.plugin.finder.DirectoryBasedPluginFinder;
import com.hw.lineage.loader.plugin.finder.PluginFinder;
import com.hw.lineage.loader.service.OtherTestService;
import com.hw.lineage.loader.service.TestService;

import com.hw.lineage.loader.utils.Preconditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @description: DefaultPluginManagerTest
 * 用于测试插件管理器 DefaultPluginManager 的功能，包括插件加载、类加载隔离等行为。
 * @author: HamaWhite
 */
public class DefaultPluginManagerTest extends PluginTestBase {

    // 使用 JUnit 的 TemporaryFolder 规则，在测试中创建临时目录，测试结束后自动清理
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    // 用于存储由插件描述符（PluginDescriptor）构建的插件集合
    private Collection<PluginDescriptor> descriptors;

    /**
     * 在测试开始前准备一个插件目录结构，并使用 DirectoryBasedPluginFinder 创建插件描述符。
     *
     * 目录结构示例：
     * <pre>
     * tmp/plugins-root/
     *          |-------------plugin-a/
     *          |             |-plugin-a.jar
     *          |
     *          |-------------plugin-b/
     *                        |-plugin-b.jar
     * </pre>
     */
    @Before
    public void setup() throws Exception {
        // 创建临时插件根目录
        File pluginRootFolder = temporaryFolder.newFolder();
        Path pluginRootFolderPath = pluginRootFolder.toPath();

        // 在根目录下创建 plugin-a 和 plugin-b 子目录
        File pluginAFolder = new File(pluginRootFolder, PLUGIN_A);
        File pluginBFolder = new File(pluginRootFolder, PLUGIN_B);

        // 验证目录创建成功
        Preconditions.checkState(pluginAFolder.mkdirs());
        Preconditions.checkState(pluginBFolder.mkdirs());

        // 将测试用的插件 JAR 文件拷贝到各自的目录中
        Files.copy(locateJarFile(PLUGIN_A_JAR).toPath(), Paths.get(pluginAFolder.toString(), PLUGIN_A_JAR));
        Files.copy(locateJarFile(PLUGIN_B_JAR).toPath(), Paths.get(pluginBFolder.toString(), PLUGIN_B_JAR));

        // 使用 DirectoryBasedPluginFinder 在根目录中查找插件，生成插件描述符
        PluginFinder descriptorsFactory = new DirectoryBasedPluginFinder(pluginRootFolderPath);
        descriptors = descriptorsFactory.findPlugins();

        // 验证是否成功加载了两个插件描述符
        Preconditions.checkState(descriptors.size() == 2);
    }

    /**
     * 测试插件加载功能，包括类加载器隔离、插件类加载、接口实现检查等。
     */
    @Test
    public void testLoadPlugin() {
        // 定义父加载器模式，用于从父加载器加载的类名集合
        String[] parentPatterns = {TestService.class.getName(), OtherTestService.class.getName()};

        // 创建插件管理器，传入插件描述符集合、父加载器和父加载模式
        PluginManager pluginManager = new DefaultPluginManager(descriptors, PARENT_CLASS_LOADER, parentPatterns);

        // 加载 TestService 接口的所有实现类，返回插件名称到实现类迭代器的映射
        Map<String, Iterator<TestService>> pluginIteratorMap = pluginManager.load(TestService.class);
        Assert.assertEquals(2, pluginIteratorMap.size()); // 验证两个插件都加载了 TestService 的实现

        // 验证每个实现类是否使用唯一的类加载器
        Set<ClassLoader> classLoaders = Collections.newSetFromMap(new IdentityHashMap<>(4));
        classLoaders.add(PARENT_CLASS_LOADER);

        // 将每个插件的 TestService 实现类转为列表
        List<TestService> testServiceListA = Lists.newArrayList(pluginIteratorMap.get(PLUGIN_A));
        List<TestService> testServiceListB = Lists.newArrayList(pluginIteratorMap.get(PLUGIN_B));

        // 验证每个插件目录中只加载了一个 TestService 实现类
        Assert.assertEquals(1, testServiceListA.size());
        Assert.assertEquals(1, testServiceListB.size());

        // 获取具体的 TestService 实例
        TestService testServiceA = testServiceListA.get(0);
        TestService testServiceB = testServiceListB.get(0);

        // 验证 TestServiceA 和 TestServiceB 的功能输出
        Assert.assertEquals("A-hello-Dynamic-A-hello", testServiceA.say("hello"));
        Assert.assertEquals("B-hello", testServiceB.say("hello"));

        // 验证 TestServiceA 和 TestServiceB 使用了不同的类加载器
        Assert.assertTrue(classLoaders.add(testServiceA.getClass().getClassLoader()));
        Assert.assertTrue(classLoaders.add(testServiceB.getClass().getClassLoader()));

        // 加载 OtherTestService 接口的所有实现类
        Map<String, Iterator<OtherTestService>> otherPluginIteratorMap = pluginManager.load(OtherTestService.class);

        // 将每个插件的 OtherTestService 实现类转为列表
        List<OtherTestService> otherTestServiceListA = Lists.newArrayList(otherPluginIteratorMap.get(PLUGIN_A));
        List<OtherTestService> otherTestServiceListB = Lists.newArrayList(otherPluginIteratorMap.get(PLUGIN_B));

        // 验证 plugin-a 中没有 OtherTestService 的实现，而 plugin-b 中有一个
        Assert.assertEquals(0, otherTestServiceListA.size());
        Assert.assertEquals(1, otherTestServiceListB.size());

        // 获取 plugin-b 中的 OtherTestService 实例并验证功能
        OtherTestService otherTestServiceB = otherTestServiceListB.get(0);
        Assert.assertEquals("Other-B-hello", otherTestServiceB.otherSay("hello"));

        // 验证 OtherTestServiceB 使用了唯一的类加载器
        Assert.assertTrue(classLoaders.add(otherTestServiceB.getClass().getClassLoader()));
    }
}
