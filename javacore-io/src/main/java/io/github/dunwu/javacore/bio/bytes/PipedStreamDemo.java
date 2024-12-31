package io.github.dunwu.javacore.bio.bytes;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 管道流示例。
 * <p>
 * 使用 {@link PipedInputStream} 和 {@link PipedOutputStream} 实现两个线程间的数据传输。
 * </p>
 *
 */
public class PipedStreamDemo {

    public static void main(String[] args) {
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        // 连接管道输入流和输出流
        try {
            sender.getOutputStream().connect(receiver.getInputStream());
        } catch (IOException e) {
            System.err.println("管道连接失败: " + e.getMessage());
            e.printStackTrace();
        }

        // 启动线程
        new Thread(sender, "Sender-Thread").start();
        new Thread(receiver, "Receiver-Thread").start();
    }

    /**
     * 数据发送线程。
     */
    static class Sender implements Runnable {
        private final PipedOutputStream outputStream;

        Sender() {
            this.outputStream = new PipedOutputStream();
        }

        @Override
        public void run() {
            String message = "Hello World!!!";
            try (PipedOutputStream out = this.outputStream) {
                System.out.println(Thread.currentThread().getName() + " 正在发送数据: " + message);
                out.write(message.getBytes());
            } catch (IOException e) {
                System.err.println("发送数据时发生错误: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * 获取管道输出流。
         *
         * @return {@link PipedOutputStream}
         */
        PipedOutputStream getOutputStream() {
            return this.outputStream;
        }
    }

    /**
     * 数据接收线程。
     */
    static class Receiver implements Runnable {
        private final PipedInputStream inputStream;

        Receiver() {
            this.inputStream = new PipedInputStream();
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            try (PipedInputStream in = this.inputStream) {
                int length = in.read(buffer);
                String receivedMessage = new String(buffer, 0, length);
                System.out.println(Thread.currentThread().getName() + " 接收到的数据: " + receivedMessage);
            } catch (IOException e) {
                System.err.println("接收数据时发生错误: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * 获取管道输入流。
         *
         * @return {@link PipedInputStream}
         */
        PipedInputStream getInputStream() {
            return this.inputStream;
        }
    }
}
