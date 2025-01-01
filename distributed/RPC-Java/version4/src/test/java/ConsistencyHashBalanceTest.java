import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import part1.Client.serviceCenter.balance.impl.ConsistencyHashBalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试一致性哈希负载均衡器 (ConsistencyHashBalance)
 */
class ConsistencyHashBalanceTest {

    private ConsistencyHashBalance consistencyHashBalance;
    private List<String> initialNodes;

    @BeforeEach
    void setUp() {
        consistencyHashBalance = new ConsistencyHashBalance();
        initialNodes = new ArrayList<>(Arrays.asList("Node1", "Node2", "Node3"));
    }

    /**
     * 测试初始化时虚拟节点的生成是否正确
     */
    @Test
    void testInitialization() {
        // 模拟初始化
        String testNode = "TestNode";
        List<String> nodes = Collections.singletonList(testNode);

        // 使用测试的 key
        String result = consistencyHashBalance.getServer("TestKey", nodes);

        // 验证结果
        assertNotNull(result, "分配的服务器不应为 null");
        assertEquals(testNode, result, "TestKey 应该被分配到 TestNode");
    }

    /**
     * 测试负载均衡的分配是否稳定（相同 key 应该分配到相同节点）
     */
    @Test
    void testConsistentHashing() {
        // 初始化节点
        String testKey = "ConsistentKey";
        String node1 = consistencyHashBalance.getServer(testKey, initialNodes);

        // 再次调用，结果应一致
        String node2 = consistencyHashBalance.getServer(testKey, initialNodes);

        assertEquals(node1, node2, "相同的 key 应该分配到相同的节点");
    }

    /**
     * 测试添加节点后的分配是否正常
     */
    @Test
    void testAddNode() {
        String newNode = "Node4";
        consistencyHashBalance.addNode(newNode);

        // 使用特定 key 测试
        String result = consistencyHashBalance.getServer("AddKey", Collections.singletonList(newNode));

        assertNotNull(result, "分配的服务器不应为 null");
        assertEquals(newNode, result, "新的节点应该正常分配到 AddKey");
    }

    /**
     * 测试删除节点后的分配是否正常
     */
    @Test
    void testRemoveNode() {
        String removedNode = "Node2";
        // 从 initialNodes 中移除被删除的节点
        initialNodes.remove(removedNode);

        // 验证 Node2 的删除行为
        consistencyHashBalance.delNode(removedNode);

        // 重新进行 key 分配
        String testKey = "RemoveKey";
        String result = consistencyHashBalance.getServer(testKey, initialNodes);

        assertNotEquals(removedNode, result, "删除的节点不应再被分配");
    }

    /**
     * 测试负载均衡器的整体行为
     */
    @Test
    void testLoadBalancerBehavior() {
        String randomKey = "RandomKey";

        // 验证分配的节点是否存在于初始节点列表中
        String result = consistencyHashBalance.getServer(randomKey, initialNodes);
        assertTrue(initialNodes.contains(result), "分配的节点应该在节点列表中");
    }

    /**
     * 测试 hash 函数的结果是否唯一且非负
     */
    @Test
    void testHashFunction() {
        String value1 = "TestValue1";
        String value2 = "TestValue2";

        int hash1 = ConsistencyHashBalance.getHash(value1);
        int hash2 = ConsistencyHashBalance.getHash(value2);

        // 验证 hash 值是否非负
        assertTrue(hash1 >= 0, "Hash 值应该为非负");
        assertTrue(hash2 >= 0, "Hash 值应该为非负");

        // 验证不同值是否生成不同 hash
        assertNotEquals(hash1, hash2, "不同的值应该生成不同的 hash");
    }
}

