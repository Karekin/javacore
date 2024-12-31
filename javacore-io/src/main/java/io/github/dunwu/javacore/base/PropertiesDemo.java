package io.github.dunwu.javacore.base;

import java.io.*;
import java.util.Properties;

/**
 * 综合演示 {@link Properties} 类的基本操作，包括设置、存储和读取属性。
 *
 * 支持以下功能：
 * 1. 设置和读取属性
 * 2. 将属性保存到普通文件
 * 3. 从普通文件读取属性
 * 4. 将属性保存到 XML 文件
 * 5. 从 XML 文件读取属性
 *
 */
public class PropertiesDemo {

    private static final String PROPERTIES_FILE = "area.properties";
    private static final String XML_FILE = "area.xml";

    public static void main(String[] args) {
        // 示例 1: 设置和读取属性
        setAndReadProperties();

        // 示例 2: 保存属性到普通文件
        savePropertiesToFile();

        // 示例 3: 从普通文件读取属性
        loadPropertiesFromFile();

        // 示例 4: 保存属性到 XML 文件
        savePropertiesToXML();

        // 示例 5: 从 XML 文件读取属性
        loadPropertiesFromXML();
    }

    /**
     * 示例 1: 设置和读取属性。
     */
    private static void setAndReadProperties() {
        Properties pro = new Properties();
        pro.setProperty("BJ", "BeiJing");
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");

        System.out.println("[设置和读取属性]");
        System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
        System.out.println("2、SC属性不存在：" + pro.getProperty("SC"));
        System.out.println("3、SC属性不存在，同时设置显示的默认值：" + pro.getProperty("SC", "没有发现"));
    }

    /**
     * 示例 2: 保存属性到普通文件。
     */
    private static void savePropertiesToFile() {
        Properties pro = new Properties();
        pro.setProperty("BJ", "BeiJing");
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");

        System.out.println("[保存属性到普通文件]");
        File file = new File(PROPERTIES_FILE);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            pro.store(fos, "Area Info");
            System.out.println("属性已保存到文件: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 示例 3: 从普通文件读取属性。
     */
    private static void loadPropertiesFromFile() {
        Properties pro = new Properties();
        File file = new File(PROPERTIES_FILE);

        System.out.println("[从普通文件读取属性]");
        try (FileInputStream fis = new FileInputStream(file)) {
            pro.load(fis);
            System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
            System.out.println("2、SH属性存在：" + pro.getProperty("SH"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 示例 4: 保存属性到 XML 文件。
     */
    private static void savePropertiesToXML() {
        Properties pro = new Properties();
        pro.setProperty("BJ", "BeiJing");
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");

        System.out.println("[保存属性到 XML 文件]");
        File file = new File(XML_FILE);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            pro.storeToXML(fos, "Area Info");
            System.out.println("属性已保存到 XML 文件: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 示例 5: 从 XML 文件读取属性。
     */
    private static void loadPropertiesFromXML() {
        Properties pro = new Properties();
        File file = new File(XML_FILE);

        System.out.println("[从 XML 文件读取属性]");
        try (FileInputStream fis = new FileInputStream(file)) {
            pro.loadFromXML(fis);
            System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
