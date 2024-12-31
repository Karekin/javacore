package io.github.dunwu.javacore.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 使用 Scanner 接收用户输入并进行基本校验的示例。
 *
 * 支持整数、小数和日期格式的输入。
 *
 */
public class ScannerDemo {

    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) { // 使用 try-with-resources 确保资源关闭

            // 接收整数输入
            System.out.print("输入整数：");
            if (scan.hasNextInt()) {
                int i = scan.nextInt();
                System.out.println("整数数据：" + i);
            } else {
                System.out.println("输入的不是整数！");
                scan.next(); // 清除错误输入
            }

            // 接收小数输入
            System.out.print("输入小数：");
            if (scan.hasNextFloat()) {
                float f = scan.nextFloat();
                System.out.println("小数数据：" + f);
            } else {
                System.out.println("输入的不是小数！");
                scan.next(); // 清除错误输入
            }

            // 接收日期输入
            System.out.print("输入日期（yyyy-MM-dd）：");
            if (scan.hasNext()) {
                String dateStr = scan.next();
                Date date = parseDate(dateStr);
                if (date != null) {
                    System.out.println("日期数据：" + date);
                } else {
                    System.out.println("输入的日期格式错误！");
                }
            } else {
                System.out.println("未输入日期！");
            }
        }
    }

    /**
     * 解析日期字符串。
     *
     * @param dateStr 日期字符串
     * @return 解析成功返回 Date 对象，否则返回 null
     */
    private static Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // 严格校验日期格式
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
