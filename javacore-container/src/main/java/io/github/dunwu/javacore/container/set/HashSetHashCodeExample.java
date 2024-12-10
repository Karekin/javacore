package io.github.dunwu.javacore.container.set;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/**
 * <p>
 * <b>问题：详见测试类 EntitySheetBoTest </b><br>
 * 修改对象的属性后，<code>hashCode</code> 发生变化，导致 <code>HashSet</code> 无法在原来的哈希桶中找到对象。
 * <br>Set 集合还是会用元素最初加入集合时生成的 <code>hashCode</code>？哪怕元素的 <code>hashCode</code> 改变，“钥匙”也不会改变？
 * </p>
 *
 * <h2>1. HashSet 的基本工作原理</h2>
 * <p>
 * <code>HashSet</code> 的核心基于哈希表。当对象被添加到 <code>HashSet</code> 时，会计算对象的 <code>hashCode</code>，并根据这个值将对象存储在相应的哈希桶中。
 * </p>
 * <ol>
 * <li>
 * <b>添加元素时：</b>
 * <ul>
 * <li>调用对象的 <code>hashCode</code> 方法，计算出一个哈希值。</li>
 * <li>根据哈希值找到对应的哈希桶。</li>
 * <li>在该桶中检查是否存在与该对象 <code>equals</code> 的元素。如果不存在，则将元素添加到桶中。</li>
 * </ul>
 * </li>
 * <li>
 * <b>查找元素时（例如 <code>contains</code> 方法）：</b>
 * <ul>
 * <li>再次调用对象的 <code>hashCode</code> 方法，根据返回的值找到对应的哈希桶。</li>
 * <li>在该桶中逐一调用 <code>equals</code> 方法检查是否存在匹配的对象。</li>
 * </ul>
 * </li>
 * </ol>
 *
 * <h2>2. HashCode 改变的影响</h2>
 * <p>当元素被添加到 <code>HashSet</code> 中时，其存储位置由添加时的 <code>hashCode</code> 决定。如果之后修改了对象的属性，导致 <code>hashCode</code> 值发生变化：</p>
 * <ol>
 * <li>
 * <b>存储的“钥匙”不变：</b>
 * <ul>
 * <li>元素在集合中存储的位置（即哈希桶）仍然是基于初始的 <code>hashCode</code>。</li>
 * <li><code>HashSet</code> 没有自动更新哈希值或重新分配哈希桶的机制。</li>
 * </ul>
 * </li>
 * <li>
 * <b>查找失败：</b>
 * <ul>
 * <li>如果调用 <code>contains</code> 方法或其他依赖 <code>hashCode</code> 的方法，集合会根据当前的 <code>hashCode</code> 计算哈希桶。</li>
 * <li>由于 <code>hashCode</code> 已改变，查找会在错误的哈希桶中进行，导致无法找到对象。</li>
 * </ul>
 * </li>
 * </ol>
 *
 * <h2>3. 示例结果</h2>
 * <pre>
 * Contains before modification: true
 * Contains after modification: false
 * Set contents: [Entity{name='entity1', value='value2'}]
 * </pre>
 * <ol>
 * <li>
 * <b>修改前：</b>
 * <ul>
 * <li>对象的 <code>hashCode</code> 和 <code>equals</code> 都匹配，能够正确找到该对象。</li>
 * </ul>
 * </li>
 * <li>
 * <b>修改后：</b>
 * <ul>
 * <li>对象的属性被修改，导致 <code>hashCode</code> 发生变化。</li>
 * <li><code>HashSet</code> 仍然使用初始 <code>hashCode</code> 存储位置，因此无法找到该对象。</li>
 * </ul>
 * </li>
 * </ol>
 *
 * <h2>4. 总结</h2>
 * <ul>
 * <li><b>HashSet 的核心特性：</b>元素的存储位置由其初次加入集合时的 <code>hashCode</code> 决定，即使对象的 <code>hashCode</code> 发生变化，存储的“钥匙”不会改变。</li>
 * <li><b>问题根源：</b>如果修改了集合中对象的属性，导致其 <code>hashCode</code> 发生变化，会破坏 <code>HashSet</code> 的哈希一致性。</li>
 * </ul>
 *
 * <h2>5. 解决方案</h2>
 * <ol>
 * <li>
 * <b>不可变对象：</b>
 * <ul>
 * <li>确保集合中的对象在生命周期内不会改变其影响 <code>hashCode</code> 的属性。</li>
 * </ul>
 * </li>
 * <li>
 * <b>重新添加：</b>
 * <ul>
 * <li>如果必须修改对象，先从集合中移除对象，修改后再重新添加。</li>
 * </ul>
 * </li>
 * <li>
 * <b>使用其他集合：</b>
 * <ul>
 * <li>例如 <code>TreeSet</code> 或 <code>LinkedList</code>，它们依赖于 <code>Comparator</code> 或迭代器，而不是 <code>hashCode</code>。</li>
 * </ul>
 * </li>
 * </ol>
 */

public class HashSetHashCodeExample {
    public static void main(String[] args) {
        Set<Entity> set = new HashSet<>();

        // 创建一个对象并添加到集合
        Entity entity = new Entity("entity1", "value1");
        set.add(entity);

        // 初次验证 contains
        System.out.println("Contains before modification: " + set.contains(entity)); // true

        // 修改对象的属性，导致 hashCode 改变
        entity.setValue("value2");

        // 再次验证 contains
        System.out.println("Contains after modification: " + set.contains(entity)); // false

        // 打印集合内容
        System.out.println("Set contents: " + set);
    }

    static class Entity {
        private final String name;
        private String value;

        public Entity(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entity)) return false;
            Entity entity = (Entity) o;
            return Objects.equals(name, entity.name) &&
                    Objects.equals(value, entity.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }

        @Override
        public String toString() {
            return "Entity{name='" + name + "', value='" + value + "'}";
        }
    }
}
