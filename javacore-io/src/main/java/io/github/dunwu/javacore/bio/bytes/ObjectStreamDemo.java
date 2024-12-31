package io.github.dunwu.javacore.bio.bytes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 对象输入输出流示例，用于对象序列化和反序列化。
 *
 * 演示：将多个 Person 对象序列化到文件中，并从文件中读取这些对象。
 *
 * @author Zhang Peng
 */
public class ObjectStreamDemo {

    public static void main(String[] args) {
        final String filepath = "object.txt";

        // 创建 Person 对象数组
        Person[] persons = {
                new Person("张三", 30),
                new Person("李四", 31),
                new Person("王五", 32)
        };

        // 序列化对象到文件
        try {
            writeObject(filepath, persons);
            System.out.println("对象序列化成功，保存到文件：" + filepath);
        } catch (IOException e) {
            System.err.println("对象序列化失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 从文件中反序列化对象
        try {
            Object[] objects = readObject(filepath);
            System.out.println("从文件中读取的对象：");
            for (Object obj : objects) {
                if (obj instanceof Person) {
                    System.out.println(obj);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("对象反序列化失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将对象数组序列化到文件。
     *
     * @param filepath 文件路径
     * @param obj      要序列化的对象数组
     * @throws IOException 如果序列化失败
     */
    public static void writeObject(String filepath, Object[] obj) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filepath)))) {
            oos.writeObject(obj);
        }
    }

    /**
     * 从文件中反序列化对象数组。
     *
     * @param filepath 文件路径
     * @return 反序列化得到的对象数组
     * @throws IOException            如果读取失败
     * @throws ClassNotFoundException 如果对象类不存在
     */
    public static Object[] readObject(String filepath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filepath)))) {
            return (Object[]) ois.readObject();
        }
    }

    /**
     * Person 类，表示一个人。
     */
    public static class Person implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "姓名：" + this.name + "；年龄：" + this.age;
        }
    }
}
