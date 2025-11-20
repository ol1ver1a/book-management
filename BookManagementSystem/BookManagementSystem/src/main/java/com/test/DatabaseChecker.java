package com.test;

import com.dao.DatabaseConnection;
import java.sql.*;

public class DatabaseChecker {
    public static void main(String[] args) {
        checkDatabase();
    }
    
    public static void checkDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("❌ 数据库连接失败");
                return;
            }
            
            System.out.println("✅ 数据库连接成功");
            
            // 检查表是否存在
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, null, new String[]{"TABLE"});
            
            System.out.println("\n📊 数据库中的表:");
            boolean hasBooks = false;
            boolean hasReaders = false;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("• " + tableName);
                if ("books".equalsIgnoreCase(tableName)) hasBooks = true;
                if ("readers".equalsIgnoreCase(tableName)) hasReaders = true;
            }
            tables.close();
            
            if (!hasBooks) {
                System.out.println("❌ books表不存在，需要创建表结构");
                createTables(conn);
            } else {
                // 检查books表中的数据
                checkBooksData(conn);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ 数据库检查失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkBooksData(Connection conn) {
        String sql = "SELECT COUNT(*) as count FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("📚 books表中有 " + count + " 条记录");
                
                if (count == 0) {
                    System.out.println("💡 表中无数据，正在插入示例数据...");
                    insertSampleData(conn);
                } else {
                    // 显示前几条数据
                    showBooksData(conn);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ 检查图书数据失败: " + e.getMessage());
        }
    }
    
    private static void showBooksData(Connection conn) {
        String sql = "SELECT isbn, title, author, available_quantity FROM books LIMIT 5";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n📖 图书数据示例:");
            while (rs.next()) {
                System.out.printf("• %s | %s | %s | 可借: %d本\n",
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("available_quantity"));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ 显示图书数据失败: " + e.getMessage());
        }
    }
    
    private static void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            
            // 创建books表
            String createBooks = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "isbn VARCHAR(20) UNIQUE NOT NULL, " +
                "title VARCHAR(200) NOT NULL, " +
                "author VARCHAR(100) NOT NULL, " +
                "publisher VARCHAR(100), " +
                "price DECIMAL(10,2), " +
                "total_quantity INTEGER DEFAULT 1, " +
                "available_quantity INTEGER DEFAULT 1, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(createBooks);
            System.out.println("✅ books表创建成功");
            
            // 创建readers表
            String createReaders = "CREATE TABLE IF NOT EXISTS readers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "reader_id VARCHAR(20) UNIQUE NOT NULL, " +
                "name VARCHAR(50) NOT NULL, " +
                "phone VARCHAR(20), " +
                "email VARCHAR(100), " +
                "max_borrow_count INTEGER DEFAULT 5, " +
                "status VARCHAR(20) DEFAULT '正常', " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(createReaders);
            System.out.println("✅ readers表创建成功");
            
            // 插入示例数据
            insertSampleData(conn);
            
        } catch (SQLException e) {
            System.err.println("❌ 创建表失败: " + e.getMessage());
        }
    }
    
    private static void insertSampleData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            
            // 插入示例图书数据
            String insertBooks = "INSERT OR IGNORE INTO books (isbn, title, author, publisher, price, total_quantity, available_quantity) VALUES " +
                "('9787111126768', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', 108.00, 5, 5), " +
                "('9787121202912', 'Head First Java', 'Kathy Sierra', '中国电力出版社', 98.00, 3, 3), " +
                "('9787302275950', '算法导论', 'Thomas H. Cormen', '清华大学出版社', 128.00, 2, 2), " +
                "('9787115351531', 'JavaScript高级程序设计', 'Nicholas C. Zakas', '人民邮电出版社', 89.00, 4, 4)";
            stmt.executeUpdate(insertBooks);
            System.out.println("✅ 示例图书数据插入成功");
            
            // 插入示例读者数据
            String insertReaders = "INSERT OR IGNORE INTO readers (reader_id, name, phone, email, max_borrow_count) VALUES " +
                "('R2023001', '张三', '13800138001', 'zhangsan@email.com', 5), " +
                "('R2023002', '李四', '13800138002', 'lisi@email.com', 5), " +
                "('R2023003', '王五', '13800138003', 'wangwu@email.com', 8)";
            stmt.executeUpdate(insertReaders);
            System.out.println("✅ 示例读者数据插入成功");
            
        } catch (SQLException e) {
            System.err.println("❌ 插入示例数据失败: " + e.getMessage());
        }
    }
}