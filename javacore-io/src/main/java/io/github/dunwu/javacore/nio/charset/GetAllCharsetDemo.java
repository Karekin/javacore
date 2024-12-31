package io.github.dunwu.javacore.nio.charset;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.SortedMap;

/**
 * 显示系统中所有可用的字符集及其别名。
 *
 * @author
 */
public class GetAllCharsetDemo {

    public static void main(String[] args) {
        // 获取全部可用的字符集
        SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();
        System.out.println("系统中可用的字符集及其别名：");

        // 遍历字符集并打印其详细信息
        availableCharsets.forEach((name, charset) -> {
            System.out.println(name);
            if (!charset.aliases().isEmpty()) {
                System.out.println("  别名：" + String.join(", ", charset.aliases()));
            } else {
                System.out.println("  无别名");
            }
            System.out.println();
        });

        System.out.println("总计可用字符集数量：" + availableCharsets.size());
    }
}
