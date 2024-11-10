package io.github.dunwu.javacore.jdk8.lambda;

import java.io.IOException;
import java.util.function.Supplier;

/**
 *  bufferPoolFactory 其实是一个带有工厂方法的 SupplierWithException，
 *  它在 get() 方法调用时执行了 createBufferPoolFactory 中的逻辑，动态创建了一个 BufferPool 实例。

 * 这种基于工厂方法和 Supplier 的写法在构建动态资源或对象时，有一些明显的优缺点：
 * 优势
 * 	1.	延迟初始化
 * 通过将 BufferPool 的创建过程封装在 Supplier 中，直到 get() 被调用时才实际创建 BufferPool。
 * 这种延迟初始化的特性非常适合某些资源或对象需要在特定场景下才能初始化的情况，避免了资源的过早创建或不必要的创建。
 * 	2.	高扩展性
 * 使用工厂方法和 Supplier 使创建逻辑具备很好的灵活性。可以根据上下文动态改变创建的参数和逻辑，
 * 甚至可以根据条件返回不同类型的对象（如不同的 BufferPool 实现），从而实现代码的高扩展性。
 * 	3.	简化依赖注入
 * 工厂方法允许在调用处指定创建逻辑，解耦了对象创建和使用代码。
 * 可以让调用方更方便地定制 BufferPool 的构建逻辑，而不需要修改 ResultPartition 的内部实现。
 * 	4.	异常处理支持
 * 由于 SupplierWithException 接口支持抛出异常，允许工厂方法在对象创建时对潜在的异常进行集中处理，简化了调用方的代码。

 * 劣势
 * 	1.	代码复杂度增加
 * 使用工厂方法和 Supplier 等 lambda 表达式会增加代码的复杂性，特别是在多层传递和嵌套调用的场景下，
 * 新手可能会难以理解调用栈和实际创建的逻辑。同时，调试时也不容易直接跟踪到对象的初始化过程。
 * 	2.	延迟初始化的副作用
 * 虽然延迟初始化是优点，但在某些场景下也可能引发问题。
 * 例如，如果 BufferPool 的创建过程复杂或依赖外部资源，可能会在调用 get() 时导致非预期的阻塞或性能问题。
 * 	3.	潜在的异常传播
 * 由于 SupplierWithException 中定义的 get() 方法允许抛出异常，
 * 如果调用时没有做好充分的异常处理，可能导致异常无法及时被捕获，影响应用程序的稳定性。
 * 	4.	不易于测试
 * 测试中可能会遇到困难，特别是在需要模拟或控制 Supplier 的返回值时。
 * 可能需要额外的 mock 工具来处理 lambda 表达式中的创建逻辑，增加了测试的复杂性。

 * 总结
 * 这种写法非常适合需要延迟初始化、灵活配置和解耦创建逻辑的场景，但在实际使用中需要权衡代码的复杂性和调试、测试的难度。
 * 如果 BufferPool 的创建逻辑非常复杂或需要依赖特定的上下文，这种写法是比较合适的，但要注意处理好异常和延迟初始化带来的潜在副作用。
 */
public class BufferPoolDemo {
    public static void main(String[] args) {
        try {
            // 使用 createBufferPoolFactory 创建一个 bufferPoolFactory 实例
            SupplierWithException<BufferPool, IOException> bufferPoolFactory = BufferPoolFactory.createBufferPoolFactory(10, 100);

            // 创建 ResultPartition 实例，传入 bufferPoolFactory
            ResultPartition partition = new ResultPartition(bufferPoolFactory);

            // 调用 setup 方法，获取并设置 bufferPool
            partition.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 模拟 BufferPool 类
    static class BufferPool {
        private final int minBuffers;
        private final int maxBuffers;

        public BufferPool(int minBuffers, int maxBuffers) {
            this.minBuffers = minBuffers;
            this.maxBuffers = maxBuffers;
        }

        @Override
        public String toString() {
            return "BufferPool{" +
                    "minBuffers=" + minBuffers +
                    ", maxBuffers=" + maxBuffers +
                    '}';
        }
    }

    // 模拟 SupplierWithException 类 (类似于标准的 Supplier，但可以抛出异常)
    @FunctionalInterface
    interface SupplierWithException<T, E extends Exception> {
        T get() throws E;
    }

    // 模拟 ResultPartition 类
    static class ResultPartition {
        private final SupplierWithException<BufferPool, IOException> bufferPoolFactory;

        // 构造函数，传入 bufferPoolFactory
        public ResultPartition(SupplierWithException<BufferPool, IOException> bufferPoolFactory) {
            this.bufferPoolFactory = bufferPoolFactory;
        }

        // setup 方法，获取 bufferPool
        public void setup() throws IOException {
            /*
                bufferPoolFactory.get()：在 ResultPartition 的 setup 方法中，调用 bufferPoolFactory.get()，
                这会触发 createBufferPoolFactory 中的 lambda 表达式执行，进而创建一个 BufferPool 实例。
             */
            BufferPool bufferPool = bufferPoolFactory.get();
            System.out.println("BufferPool setup completed: " + bufferPool);
        }
    }

    /*
        模拟创建 bufferPool 的方法:
        createBufferPoolFactory 接受两个参数 minBuffers 和 maxBuffers，并返回一个 lambda 表达式。
        这种 lambda 表达式实现了 SupplierWithException<BufferPool, IOException> 接口，其 get() 方法会创建并返回一个新的 BufferPool 实例。
     */
    static class BufferPoolFactory {
        public static SupplierWithException<BufferPool, IOException> createBufferPoolFactory(int minBuffers, int maxBuffers) {
            return () -> new BufferPool(minBuffers, maxBuffers);
        }
    }
}





