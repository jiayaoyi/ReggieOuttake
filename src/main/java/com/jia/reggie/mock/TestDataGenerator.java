package com.jia.reggie.mock;

import java.util.Random;

/**
 * @author kk
 */
public class TestDataGenerator {
    public static void main(String[] args) {
        generateTestData(100);
    }

    public static void generateTestData(int n) {
        Random random = new Random();
        for (int i = 2; i <= n+1; i++) {  // ID starts from 2
            String name = "用户" + (i - 1);  //
            String username = "username" + (i - 1);
            String phone = "13" + String.format("%09d", random.nextInt(1_000_000_000));
            String sex = i % 2 == 0 ? "男" : "女";
            String year = String.valueOf(1950 + random.nextInt(53));
            String month = String.format("%02d", 1 + random.nextInt(12));
            String day = String.format("%02d", 1 + random.nextInt(29));  // For simplicity, assume all months have 28 days
            String idNumber = "420626" + year + month + day + ((i - 1) % 10);

            String sql = String.format("INSERT INTO `employee` (`id`, `name`,`username`, `phone`, `sex`, `id_number`) VALUES (%d, '%s','%s', '%s', '%s', '%s');", i, name,username, phone, sex, idNumber);
            System.out.println(sql);
        }
    }
}
