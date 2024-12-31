package io.github.dunwu.javacore.concurrent.current.features.concurrenthashmap;

/**
 * 描述:
 * C map Test

 * 这段代码与 ConcurrentHashMap 的关系主要体现在以下方面：
 * 1、容量调整：ConcurrentHashMap 中通过计算 2 的幂次方的合适容量来优化哈希表性能，
 *      而代码中的 tableSizeFor 方法实现了类似的功能。
 * 2、哈希冲突处理：ConcurrentHashMap 中使用类似的 spread 方法来扩展哈希值的分布，减少冲突。
 * 3、同步机制：虽然代码中使用 synchronized 来控制线程同步，
 *      但 ConcurrentHashMap 使用了更加高效的分段锁或 CAS 操作来支持高并发访问。
 * 因此，这段代码主要是展示了一个简化的哈希表的实现，虽然没有直接实现 ConcurrentHashMap 的并发特性，
 *      但它展示的哈希计算和容量调整方法与 ConcurrentHashMap 内部的实现有很多相似之处。
 *
 * @author zed
 * @since 2019-06-04 2:30 PM
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        Object o = new Object(); // 创建一个Object对象用于同步块中
        try{
            // 使用synchronized关键字对ConcurrentHashMapTest类进行同步
            synchronized (ConcurrentHashMapTest.class){
                // 当前线程在此处等待300秒（300000毫秒）
                o.wait(300000);
            }

        }catch (Exception e){
            // 捕获并打印异常信息
            System.out.println(e);
        }

        // 调用tableSizeFor方法，输出7的合适容量（转换为2的幂次方）
        System.out.println(tableSizeFor(7));

        // 调用spread方法，输出1212321334经过散列转换后的值
        System.out.println(spread(1212321334));

        // 计算(16 - 1) & spread(1212321334)，并输出结果
        // 这里是通过位运算计算数组的位置
        System.out.println((16 - 1) & spread(1212321334));

        // 等同于 hash值对长度16取模
        System.out.println(spread(1212321334) % 16);
    }

    // 定义最大容量常量（2^30）
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 用于正常节点哈希值的有效位数
     */
    private static final int HASH_BITS = 0x7fffffff;

    /**
     * 计算合适的表容量大小，使其为2的幂次方。
     * @param c 输入的大小（容量）
     * @return int 返回合适的容量大小（2的幂次方）
     */
    private static int tableSizeFor(int c) {
        int n = c - 1;  // 首先将输入值减1
        n |= n >>> 1;   // 对n进行位移操作，逐步扩大其有效位数
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        // 最终返回合适的容量，若n小于0，返回1；若n超过最大容量，返回最大容量；否则返回n+1
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 对hashCode的低16位与高16位进行异或运算，进行hash值扩展。
     * 目的是为了更均匀的散列分布，避免hash冲突。
     * @param h hashCode
     * @return int 返回转换后的散列值
     */
    private static int spread(int h) {
        // 通过异或操作和掩码操作，返回低16位和高16位进行散列后的结果
        return (h ^ (h >>> 16)) & HASH_BITS;
    }
}
