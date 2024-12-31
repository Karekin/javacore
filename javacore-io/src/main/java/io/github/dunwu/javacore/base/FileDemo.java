package io.github.dunwu.javacore.base;

import java.io.File;
import java.io.IOException;

/**
 * 示例：File 类的常用方法
 */
public class FileDemo {

    /**
     * 创建新文件。
     *
     * @param pathname 文件路径
     */
    public static void createNewFile(String pathname) {
        if (pathname == null || pathname.trim().isEmpty()) {
            System.out.println("路径无效，无法创建文件。");
            return;
        }
        File file = new File(pathname);
        try {
            // 尝试创建新文件
            if (file.createNewFile()) {
                System.out.println("文件创建成功：" + file.getAbsolutePath());
            } else {
                System.out.println("文件已存在，未创建：" + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("创建文件失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建新目录。
     *
     * @param pathname 目录路径
     */
    public static void mkdir(String pathname) {
        if (pathname == null || pathname.trim().isEmpty()) {
            System.out.println("路径无效，无法创建目录。");
            return;
        }
        File dir = new File(pathname);
        if (dir.mkdir()) {
            System.out.println("目录创建成功：" + dir.getAbsolutePath());
        } else {
            System.out.println("目录创建失败：" + dir.getAbsolutePath());
        }
    }

    /**
     * 列出当前目录中的文件名。
     */
    public static void list() {
        File currentDir = new File(".");
        String[] fileNames = currentDir.list();
        if (fileNames != null && fileNames.length > 0) {
            System.out.println("当前目录中的文件列表：");
            for (String name : fileNames) {
                System.out.println(name);
            }
        } else {
            System.out.println("当前目录为空。");
        }
    }

    /**
     * 列出当前目录中的文件的绝对路径和相对路径。
     */
    public static void listFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles();
        if (files != null && files.length > 0) {
            System.out.println("文件的绝对路径：");
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }

            System.out.println("文件的相对路径：");
            for (File file : files) {
                try {
                    System.out.println(file.getCanonicalPath());
                } catch (IOException e) {
                    System.err.println("无法解析文件的相对路径：" + e.getMessage());
                }
            }
        } else {
            System.out.println("当前目录为空。");
        }
    }

    /**
     * 删除文件或文件夹。
     *
     * @param pathname 文件或文件夹路径
     */
    public static void delete(String pathname) {
        if (pathname == null || pathname.trim().isEmpty()) {
            System.out.println("路径无效，无法删除文件或文件夹。");
            return;
        }
        File file = new File(pathname);
        if (file.delete()) {
            System.out.println("成功删除：" + file.getAbsolutePath());
        } else {
            System.out.println("删除失败：" + file.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        // 示例测试
        createNewFile("test.txt");
        mkdir("testDir");
        list();
        listFiles();
        delete("test.txt");
        delete("testDir");
    }
}
