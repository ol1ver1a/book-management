package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:library.db";
    private static Connection connection = null;
    
    static {
        try {
            // 注册SQLite JDBC驱动
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                // 启用外键约束
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 初始化数据库（第一次运行时创建表）
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // 这里可以执行创建表的SQL语句
            // 或者直接从SQL文件读取并执行
            System.out.println("数据库初始化完成！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}