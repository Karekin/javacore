package hashset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * 对于集合 <code>entitySheetBos</code> 和对象 <code>currEntitySheetBo</code>，从数据上看明明能包含，
 * 但 <code>entitySheetBos.contains(currEntitySheetBo)</code> 还是返回 <code>false</code>。
 * 这是否和上一步的递归过程 <code>recurseBackward4Carried</code> 改变了对象中的 <code>sheetId</code> 集合有关系？
 * </p>
 *
 * <h3>1. 问题原因</h3>
 * <p>
 * 问题的根源可能在于 <code>recurseBackward4Carried</code> 方法中修改了 <code>EntitySheetBo</code> 对象中的 <code>sheetId</code> 集合，
 * 导致 <code>entitySheetBos.contains(currEntitySheetBo)</code> 返回 <code>false</code>。
 * 这是因为 <code>HashSet</code> 的 <code>contains</code> 方法依赖于 <code>hashCode</code> 和 <code>equals</code> 方法，
 * 如果对象在被放入 <code>HashSet</code> 后发生了属性的修改（如 <code>sheetId</code> 集合内容的变化），会破坏其在 <code>HashSet</code> 中的哈希一致性。
 * </p>
 *
 * <h4>1.1. HashSet 的哈希一致性要求</h4>
 * <ul>
 * <li><code>HashSet</code> 使用 <code>hashCode</code> 方法定位对象在哈希桶中的位置。</li>
 * <li>如果对象的 <code>hashCode</code> 值发生变化，可能会导致对象无法被正确找到，即使对象内容相同，<code>contains</code> 也可能返回 <code>false</code>。</li>
 * </ul>
 *
 * <h4>1.2. 在 recurseBackward4Carried 中修改了对象</h4>
 * <ul>
 * <li>调用 <code>currEntitySheetBo.getSheetId().add(current.getCarriedCode())</code> 修改了 <code>sheetId</code> 集合。</li>
 * <li><code>EntitySheetBo</code> 的 <code>hashCode</code> 方法依赖于 <code>sheetId</code> 的内容，因此 <code>hashCode</code> 值会发生变化。</li>
 * </ul>
 *
 * <h4>1.3. 导致的后果</h4>
 * <ul>
 * <li>对象被放入 <code>entitySheetBos</code> 后，哈希值已经计算并存储。</li>
 * <li>修改对象的 <code>sheetId</code> 集合后，<code>hashCode</code> 发生变化，<code>HashSet</code> 无法在原来的哈希桶中找到对象。</li>
 * </ul>
 *
 * <h3>2. 解决方案</h3>
 * <h4>2.1. 避免修改依赖于 hashCode 的属性</h4>
 * <p>最简单的方案是让 <code>EntitySheetBo</code> 的 <code>sheetId</code> 集合不可变，这样对象的 <code>hashCode</code> 值不会受到影响。</p>
 * <pre>
 * EntitySheetBo newEntitySheetBo = new EntitySheetBo(nodeId, dimCode, new HashSet<>(currEntitySheetBo.getSheetId()));
 * newEntitySheetBo.getSheetId().add(current.getCarriedCode());
 * entitySheetBos.add(newEntitySheetBo);
 * </pre>
 *
 * <h4>2.2. 改用 TreeSet 或自定义比较器</h4>
 * <p>如果修改对象的属性是不可避免的，可以考虑使用 <code>TreeSet</code>，它依赖于比较器而不是 <code>hashCode</code>。</p>
 * <pre>
 * Set&lt;EntitySheetBo&gt; entitySheetBos = new TreeSet&lt;&gt;(Comparator.comparing(EntitySheetBo::getNodeId)
 *         .thenComparing(EntitySheetBo::getEntity)
 *         .thenComparing(entity -> String.join(",", entity.getSheetId())));
 * </pre>
 *
 * <h4>2.3. 在使用前重新创建集合</h4>
 * <p>如果必须修改对象，可以在使用前创建一个新的集合，强制更新对象的哈希值。例如：</p>
 * <pre>
 * Set&lt;EntitySheetBo&gt; updatedSet = new HashSet&lt;&gt;(entitySheetBos);
 * return updatedSet.contains(currEntitySheetBo);
 * </pre>
 * <p>但这种方式效率较低，不推荐。</p>
 *
 * <h3>3. 最终推荐方案</h3>
 * <p>将修改限制在对象之外，避免直接修改集合中的对象。以下是修改后的 <code>recurseBackward4Carried</code> 方法：</p>
 *
 * <h3>4. 总结</h3>
 * <ul>
 * <li>直接修改对象的属性会导致 <code>HashSet</code> 的哈希一致性问题。</li>
 * <li>推荐通过创建新的 <code>EntitySheetBo</code> 对象替代原对象，确保集合中的对象不可变。</li>
 * <li>如果修改对象不可避免，可以考虑改用 <code>TreeSet</code> 或其他解决方案。</li>
 * </ul>
 */

public class EntitySheetBoTest {

    @Test
    void testSetEquality() {
        // 创建第一个集合
        Set<String> set1 = new HashSet<>();
        set1.add("A");
        set1.add("B");
        set1.add("C");

        // 创建第二个集合，元素内容相同，但对象地址不同
        Set<String> set2 = new HashSet<>();
        set2.add("C");
        set2.add("B");
        set2.add("A");

        // 验证两个集合的对象地址不同：使用引用比较 (==)，检查两个对象是否是同一个内存引用。
        Assertions.assertNotSame(set1, set2, "set1 and set2 should not be the same object");

        // 验证两个集合的内容相等：使用对象的内容比较 (equals 方法，Objects.equals(expected, actual))，检查两个对象的内容是否相等。
        Assertions.assertEquals(set1, set2, "set1 and set2 should have the same elements");
    }

    @Test
    void testHashSetContainsAfterModification() {
        // 创建一个 HashSet
        Set<EntitySheetBo> entitySheetBos = new HashSet<>();

        // 创建一个对象并添加到 HashSet 中
        EntitySheetBo entity = new EntitySheetBo("node1", "entity1", new HashSet<>());
        entitySheetBos.add(entity);

        // 验证初始状态下 HashSet 的 contains 方法
        Assertions.assertTrue(entitySheetBos.contains(entity), "HashSet should contain the object initially");

        // 修改对象的属性
        entity.getSheetId().add("sheet1");

        // 修改后验证 contains 方法
        Assertions.assertFalse(entitySheetBos.contains(entity), "HashSet should not find the object after modification");

        // 打印 HashSet 内容
        System.out.println("HashSet contents: " + entitySheetBos);
    }

    /**
     * 使用TreeSet自定义比较器，避免使用hashCode（治标不治本，根本方法还是得避免修改集合中元素属性的行为！）
     */
    @Test
    void testTreeSetContainsWithComparator() {
        // 创建一个 TreeSet，使用 Comparator 比较对象的内容
        Set<EntitySheetBo> entitySheetBos = new TreeSet<>(Comparator.comparing(EntitySheetBo::getNodeId)
                .thenComparing(EntitySheetBo::getEntity)
                .thenComparing(entity -> String.join(",", entity.getSheetId())));

        // 创建一个对象并添加到 TreeSet 中
        EntitySheetBo entity = new EntitySheetBo("node1", "entity1", new HashSet<>());
        entitySheetBos.add(entity);

        // 验证初始状态下 TreeSet 的 contains 方法
        Assertions.assertTrue(entitySheetBos.contains(entity), "TreeSet should contain the object initially");

        // 修改对象的属性
        entity.getSheetId().add("sheet1");

        // 修改后验证 contains 方法
        Assertions.assertTrue(entitySheetBos.contains(entity), "TreeSet should not find the object after modification");

        // 打印 TreeSet 内容
        System.out.println("TreeSet contents: " + entitySheetBos);
    }

    /**
     * 向后（开始节点的方向）递归（取沿途所有对象）
     * @param toProcess: 待处理的节点-流程维对象
     * @return nodesMap: 节点id到有关系流程维的映射
     */
    private Set<EntitySheetBo> wrongRecurseBackward4Carried(List<WFNodeCarriedBo> toProcess) {
        Set<EntitySheetBo> entitySheetBos = new HashSet<>();
        Set<WFNodeCarriedBo> visited = new HashSet<>();
        List<WFNodeCarriedBo> cloneToProcess = new ArrayList<>(toProcess);

        while (!cloneToProcess.isEmpty()) {
            WFNodeCarriedBo current = cloneToProcess.remove(cloneToProcess.size() - 1);
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            String nodeId = current.getNodeId();
            String dimCode = current.getDimCode();

            EntitySheetBo currEntitySheetBo = entitySheetBos.stream()
                    .filter(entitySheetBo -> nodeId.equals(entitySheetBo.getNodeId()) && dimCode.equals(entitySheetBo.getEntity()))
                    .findFirst()
                    .orElseGet(() -> {
                        EntitySheetBo newEntitySheetBo = new EntitySheetBo(nodeId, dimCode, new HashSet<>());
                        entitySheetBos.add(newEntitySheetBo);
                        return newEntitySheetBo;
                    });

            currEntitySheetBo.getSheetId().add(current.getCarriedCode());

            current.getPrevNodeCarriedBo().stream()
                    .filter(flowDim -> !visited.contains(flowDim))
                    .forEach(cloneToProcess::add);
        }

        return entitySheetBos;
    }

    private Set<EntitySheetBo> correctRecurseBackward4Carried(List<WFNodeCarriedBo> toProcess) {
        Set<EntitySheetBo> entitySheetBos = new HashSet<>();
        Set<WFNodeCarriedBo> visited = new HashSet<>();
        List<WFNodeCarriedBo> cloneToProcess = new ArrayList<>(toProcess);

        while (!cloneToProcess.isEmpty()) {
            WFNodeCarriedBo current = cloneToProcess.remove(cloneToProcess.size() - 1);
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            String nodeId = current.getNodeId();
            String dimCode = current.getDimCode();

            // 找到当前对应的 EntitySheetBo 或创建一个新对象
            EntitySheetBo existingEntitySheetBo = entitySheetBos.stream()
                    .filter(entitySheetBo -> nodeId.equals(entitySheetBo.getNodeId()) && dimCode.equals(entitySheetBo.getEntity()))
                    .findFirst()
                    .orElse(null);

            // 如果存在，创建一个新的对象代替旧对象，避免直接修改集合中元素的属性值，否则会出现在哈希桶中找不到元素的情况
            if (existingEntitySheetBo != null) {
                // 创建一个新对象，包含原有的 sheetId 和新的 current.getCarriedCode()
                Set<String> updatedSheetId = new HashSet<>(existingEntitySheetBo.getSheetId());
                updatedSheetId.add(current.getCarriedCode());
                EntitySheetBo updatedEntitySheetBo = new EntitySheetBo(nodeId, dimCode, updatedSheetId);

                // 用新对象替换旧对象
                entitySheetBos.remove(existingEntitySheetBo);
                entitySheetBos.add(updatedEntitySheetBo);
            } else {
                // 如果不存在，直接创建并添加新对象
                EntitySheetBo newEntitySheetBo = new EntitySheetBo(nodeId, dimCode, new HashSet<>(Collections.singleton(current.getCarriedCode())));
                entitySheetBos.add(newEntitySheetBo);
            }

            // 处理前置节点
            current.getPrevNodeCarriedBo().stream()
                    .filter(flowDim -> !visited.contains(flowDim))
                    .forEach(cloneToProcess::add);
        }

        return entitySheetBos;
    }
}
