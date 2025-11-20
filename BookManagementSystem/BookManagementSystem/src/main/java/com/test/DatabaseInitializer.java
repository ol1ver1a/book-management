package com.test;

import java.sql.*;

public class DatabaseInitializer {
    public static void main(String[] args) {
        createNewDatabase();
    }
    
    public static void createNewDatabase() {
        String url = "jdbc:sqlite:library.db";
        
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("✅ 新的数据库文件创建成功: library.db");
                createTables(conn);
                insertSampleData(conn);
                System.out.println("🎉 数据库初始化完成！");
            }
        } catch (SQLException e) {
            System.err.println("❌ 创建数据库失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTables(Connection conn) {
        String[] createTableSQLs = {
            // 图书表
            "CREATE TABLE IF NOT EXISTS books (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "isbn VARCHAR(20) UNIQUE NOT NULL, " +
            "title VARCHAR(200) NOT NULL, " +
            "author VARCHAR(100) NOT NULL, " +
            "publisher VARCHAR(100), " +
            "price DECIMAL(10,2), " +
            "total_quantity INTEGER DEFAULT 1, " +
            "available_quantity INTEGER DEFAULT 1, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)",
            
            // 读者表
            "CREATE TABLE IF NOT EXISTS readers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "reader_id VARCHAR(20) UNIQUE NOT NULL, " +
            "name VARCHAR(50) NOT NULL, " +
            "phone VARCHAR(20), " +
            "email VARCHAR(100), " +
            "max_borrow_count INTEGER DEFAULT 5, " +
            "status VARCHAR(20) DEFAULT '正常', " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)",
            
            // 借阅记录表
            "CREATE TABLE IF NOT EXISTS borrow_records (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "record_id VARCHAR(30) UNIQUE NOT NULL, " +
            "reader_id VARCHAR(20) NOT NULL, " +
            "book_isbn VARCHAR(20) NOT NULL, " +
            "borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "due_date DATETIME NOT NULL, " +
            "return_date DATETIME, " +
            "fine_amount DECIMAL(10,2) DEFAULT 0.00, " +
            "status VARCHAR(20) DEFAULT '借阅中', " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)",
            
            // 管理员表
            "CREATE TABLE IF NOT EXISTS admins (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password VARCHAR(100) NOT NULL, " +
            "real_name VARCHAR(50), " +
            "role VARCHAR(20) DEFAULT '管理员', " +
            "last_login DATETIME, " +
            "status VARCHAR(20) DEFAULT '激活', " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)",
            
            // 系统日志表
            "CREATE TABLE IF NOT EXISTS system_logs (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "admin_id INTEGER, " +
            "action_type VARCHAR(50) NOT NULL, " +
            "action_description TEXT NOT NULL, " +
            "ip_address VARCHAR(45), " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        };
        
        try (Statement stmt = conn.createStatement()) {
            for (String sql : createTableSQLs) {
                stmt.execute(sql);
            }
            System.out.println("✅ 所有表创建成功");
        } catch (SQLException e) {
            System.err.println("❌ 创建表失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertSampleData(Connection conn) {
        String[] insertSQLs = {
            // 插入管理员数据
            "INSERT OR IGNORE INTO admins (username, password, real_name, role) VALUES " +
            "('admin', '123456', '系统管理员', '超级管理员'), " +
            "('librarian', '123456', '图书管理员', '管理员')",
            
            // 插入图书数据
            "INSERT OR IGNORE INTO books (isbn, title, author, publisher, price, total_quantity, available_quantity) VALUES " +
            "('9787111126768', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', 108.00, 5, 5), " +
            "('9787121202912', 'Head First Java', 'Kathy Sierra', '中国电力出版社', 98.00, 3, 3), " +
            "('9787302275950', '算法导论', 'Thomas H. Cormen', '清华大学出版社', 128.00, 2, 2), " +
            "('9787115351531', 'JavaScript高级程序设计', 'Nicholas C. Zakas', '人民邮电出版社', 89.00, 4, 4), " +
            "('9787302423282', 'Python编程：从入门到实践', 'Eric Matthes', '清华大学出版社', 89.00, 6, 6), " +
            "('9787020159532', '三体', '刘慈欣', '重庆出版社', 23.00, 8, 8), " +
            "('9787544291170', '百年孤独', '加西亚·马尔克斯', '南海出版公司', 39.50, 4, 4), " +
            "('9787208061644', '追风筝的人', '卡勒德·胡赛尼', '上海人民出版社', 29.00, 5, 5)",
            
            // 插入读者数据
            "INSERT OR IGNORE INTO readers (reader_id, name, phone, email, max_borrow_count) VALUES " +
            "('R2023001', '张三', '13800138001', 'zhangsan@email.com', 5), " +
            "('R2023002', '李四', '13800138002', 'lisi@email.com', 5), " +
            "('R2023003', '王五', '13800138003', 'wangwu@email.com', 8), " +
            "('R2023004', '赵六', '13800138004', 'zhaoliu@email.com', 5), " +
            "('R2023005', '钱七', '13800138005', 'qianqi@email.com', 3)",
            
            // 插入借阅记录数据
            "INSERT OR IGNORE INTO borrow_records (record_id, reader_id, book_isbn, borrow_date, due_date) VALUES " +
            "('BR20231201001', 'R2023001', '9787111126768', '2023-12-01 10:00:00', '2023-12-31 10:00:00'), " +
            "('BR20231202001', 'R2023002', '9787121202912', '2023-12-02 14:20:00', '2024-01-01 14:20:00')"
        };
        
        try (Statement stmt = conn.createStatement()) {
            for (String sql : insertSQLs) {
                int affectedRows = stmt.executeUpdate(sql);
                System.out.println("✅ 插入数据，影响行数: " + affectedRows);
            }
            System.out.println("✅ 示例数据插入完成");
            
            // 显示统计数据
            showDataCounts(conn);
            
        } catch (SQLException e) {
            System.err.println("❌ 插入示例数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void showDataCounts(Connection conn) {
        String[] countSQLs = {
            "SELECT '图书' as type, COUNT(*) as count FROM books",
            "SELECT '读者' as type, COUNT(*) as count FROM readers",
            "SELECT '管理员' as type, COUNT(*) as count FROM admins",
            "SELECT '借阅记录' as type, COUNT(*) as count FROM borrow_records"
        };
        
        try (Statement stmt = conn.createStatement()) {
            System.out.println("\n📊 数据库统计:");
            for (String sql : countSQLs) {
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    System.out.printf("• %s: %d 条记录\n", rs.getString("type"), rs.getInt("count"));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ 统计数据显示失败: " + e.getMessage());
        }
    }
}