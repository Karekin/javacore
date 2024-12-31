package io.github.dunwu.javacore.nio.lock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 演示使用 {@link FileLock} 对文件进行独占锁操作。
 *
 * 功能：
 * 1. 锁定文件。
 * 2. 等待指定时间后释放锁。
 *
 * 注意：
 * - 需要在支持 NIO 的文件系统上运行。
 * - 如果文件被其他进程锁定，可能无法获得锁。
 *
 * @author
 */
public class FileLockDemo {

    public static void main(String[] args) {
        File file = new File("out.txt"); // 使用相对路径
        System.out.println("准备锁定文件：" + file.getAbsolutePath());

        try (FileOutputStream output = new FileOutputStream(file, true);
             FileChannel fileChannel = output.getChannel()) {

            // 尝试对文件进行独占锁操作
            FileLock lock = fileChannel.tryLock();
            if (lock != null) {
                System.out.println(file.getName() + " 文件已被锁定，模拟占用 5 秒...");
                Thread.sleep(5000); // 模拟占用时间
                lock.release(); // 释放锁
                System.out.println(file.getName() + " 文件锁已释放。");
            } else {
                System.out.println("未能锁定文件，可能已被其他进程占用。");
            }
        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("线程被中断：" + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
