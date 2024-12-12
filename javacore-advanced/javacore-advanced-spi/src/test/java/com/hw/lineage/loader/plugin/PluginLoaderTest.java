package com.hw.lineage.loader.plugin;

import com.hw.lineage.loader.PluginTestBase;
import com.hw.lineage.loader.service.OtherTestService;
import com.hw.lineage.loader.service.TestService;
import com.hw.lineage.loader.service.impl.plugina.TestServiceA;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

/**
 * @description: PluginLoaderTest
 * 该类用于测试插件加载器（PluginLoader）的功能，包括类加载隔离、插件类加载器的创建与验证等。
 * @author: HamaWhite
 */
public class PluginLoaderTest extends PluginTestBase {

    @Test
    public void testLoadPlugin() throws Exception {
        // 创建指向插件A的Jar文件的URL
        URL classpathA = createPluginJarURLFromString(PLUGIN_A_JAR);
        // parentPatterns用于指定哪些类应当从父类加载器加载（在此为TestService和OtherTestService）
        String[] parentPatterns = {TestService.class.getName(), OtherTestService.class.getName()};

        // 创建一个插件描述符对象，描述插件A使用的类路径和父类加载规则
        PluginDescriptor pluginDescriptorA =
                new PluginDescriptor(PLUGIN_A, new URL[]{classpathA}, parentPatterns);
        // 创建插件类加载器，为插件A专用
        URLClassLoader pluginClassLoaderA =
                PluginLoader.createPluginClassLoader(pluginDescriptorA, PARENT_CLASS_LOADER, new String[0]);

        // 验证插件类加载器与父加载器不相同 TODO parentPatterns 规定了TestService使用父类加载器，难道没生效？
        Assert.assertNotEquals(PARENT_CLASS_LOADER, pluginClassLoaderA);

        // 使用插件类加载器创建PluginLoader实例，用于加载插件中实现的TestService接口的服务类
        final PluginLoader pluginLoaderA = new PluginLoader(PLUGIN_A, pluginClassLoaderA);

        // 从插件加载器中加载TestService的实现类，此处返回一个迭代器
        Iterator<TestService> testServiceIteratorA = pluginLoaderA.load(TestService.class);

        // 确认有实现类可提供
        Assert.assertTrue(testServiceIteratorA.hasNext());
        // 获取第一个TestService实现实例（应为TestServiceA的一个实例，但通过隔离类加载器加载）
        TestService testServiceA = testServiceIteratorA.next();
        // 调用服务方法并验证返回结果
        Assert.assertEquals("A-hello-Dynamic-A-hello", testServiceA.say("hello"));

        // 确认没有更多的TestService实现类
        Assert.assertFalse(testServiceIteratorA.hasNext());
        // 验证加载的类的全限定名是否与预期一致（TestServiceA）
        Assert.assertEquals(TestServiceA.class.getCanonicalName(), testServiceA.getClass().getCanonicalName());

        // 验证插件实例的类加载器为我们刚创建的pluginClassLoaderA
        Assert.assertEquals(pluginClassLoaderA, testServiceA.getClassLoader());
        Assert.assertEquals(pluginClassLoaderA, testServiceA.getClass().getClassLoader());

        /**
         * 以下断言看起来比较奇怪，但实际目的是验证类加载隔离。
         * 由于PluginLoader使用“子优先”模式加载类（child-first class loading），
         * 同名类被不同的类加载器加载后，实例之间不应当相互兼容。
         * 因此，即使testServiceA表面上是TestServiceA类的实例，但它并不属于当前
         * 测试类的类加载器上下文下的TestServiceA类，而是来自插件加载器下的版本。
         */
        Assert.assertFalse(testServiceA instanceof TestServiceA);

        // 测试不同的PluginLoader对同一插件的类加载隔离
        // 再次创建一个新PluginLoader，但使用相同的插件描述符
        PluginLoader secondPluginLoaderA =
                PluginLoader.create(pluginDescriptorA, PARENT_CLASS_LOADER, new String[0]);

        // 从新创建的PluginLoader中再次加载TestService实现类
        TestService secondTestServiceA = secondPluginLoaderA.load(TestService.class).next();
        // 验证返回结果与之前一致
        Assert.assertEquals("A-hello-Dynamic-A-hello", secondTestServiceA.say("hello"));

        // 虽然类的全限定名相同，但因为是通过不同类加载器加载，因此类对象不会相同。
        Assert.assertEquals(
                testServiceA.getClass().getCanonicalName(),
                secondTestServiceA.getClass().getCanonicalName());
        // 验证类对象不相等（不同类加载器导致类对象不相同）
        Assert.assertNotEquals(testServiceA.getClass(), secondTestServiceA.getClass());
    }

    @Test
    public void testClose() throws MalformedURLException {
        // 创建插件A的URL
        URL classpathA = createPluginJarURLFromString(PLUGIN_A_JAR);
        // 定义从父加载器加载的类名模式
        String[] parentPatterns = {TestService.class.getName()};
        PluginDescriptor pluginDescriptorA =
                new PluginDescriptor(PLUGIN_A, new URL[]{classpathA}, parentPatterns);

        // 创建插件类加载器
        URLClassLoader pluginClassLoaderA =
                PluginLoader.createPluginClassLoader(
                        pluginDescriptorA, PARENT_CLASS_LOADER, new String[0]);

        // 创建插件加载器实例
        PluginLoader pluginLoaderA = new PluginLoader(PLUGIN_A, pluginClassLoaderA);
        // 关闭插件加载器，此操作应当释放资源并使类加载器不可用
        pluginLoaderA.close();

        // 验证关闭后无法通过该类加载器加载类，此处尝试加载junit.framework.Test将导致ClassNotFoundException
        Assert.assertThrows(
                ClassNotFoundException.class,
                () -> pluginClassLoaderA.loadClass(junit.framework.Test.class.getName()));
    }
}
