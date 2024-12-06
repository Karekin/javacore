package io.github.dunwu.javacore.spi.simple;

import java.util.ServiceLoader;

/**
 * Java SPI Demo
 * <p>
 * 【如何工作】
 * <p>
 * 定义服务接口：首先定义一个服务接口，它将作为SPI的契约。
 * <p>
 * 实现服务接口：然后，开发者提供一个或多个该接口的实现。
 * <p>
 * 配置服务提供者：在resources/META-INF/services目录下，为每个服务接口创建一个名为接口全限定名的文件。
 *      文件内容是实现该接口的所有提供者的全限定名，每个名称占一行。
 * <p>
 * 使用服务加载器：在应用程序中，使用java.util.ServiceLoader类来加载这些服务。
 *      ServiceLoader读取配置文件并实例化服务。
 */
public class SpiDemo {

    public static void main(String[] args) {
        ServiceLoader<DataStorage> serviceLoader = ServiceLoader.load(DataStorage.class);
        System.out.println("============ Java SPI 测试============");
        serviceLoader.forEach(loader -> System.out.println(loader.search("Yes Or No")));
    }

}
