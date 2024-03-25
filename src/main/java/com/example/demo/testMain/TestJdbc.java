package com.example.demo.testMain;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChangLF 2023/12/23
 */
public class TestJdbc {

    public static void main(String[] args) throws ClassNotFoundException {
        // 加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/changlf?characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        // 获取数据库连接，try-with-resource关闭资源
        try (Connection conn = DriverManager.getConnection(url, "root", "chang123");
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from user limit 1")) {
            resultSet.getString("username");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
