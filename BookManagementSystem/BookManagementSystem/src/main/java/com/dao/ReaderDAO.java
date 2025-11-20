package com.dao;

import com.model.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {
    
    public void saveReaders(List<Reader> readers) {
        // 不再使用文件存储
    }
    
    public List<Reader> loadReaders() {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT reader_id, name, phone, email, max_borrow_count FROM readers WHERE status = '正常'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reader reader = new Reader(
                    rs.getString("reader_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                reader.setMaxBorrowCount(rs.getInt("max_borrow_count"));
                readers.add(reader);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;
    }
    
    public boolean registerReader(Reader reader) {
        String sql = "INSERT INTO readers (reader_id, name, phone, email, max_borrow_count) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reader.getReaderId());
            pstmt.setString(2, reader.getName());
            pstmt.setString(3, reader.getPhone());
            pstmt.setString(4, reader.getEmail());
            pstmt.setInt(5, reader.getMaxBorrowCount());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateReader(Reader reader) {
        String sql = "UPDATE readers SET name = ?, phone = ?, email = ?, max_borrow_count = ? WHERE reader_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reader.getName());
            pstmt.setString(2, reader.getPhone());
            pstmt.setString(3, reader.getEmail());
            pstmt.setInt(4, reader.getMaxBorrowCount());
            pstmt.setString(5, reader.getReaderId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteReader(String readerId) {
        String sql = "UPDATE readers SET status = '注销' WHERE reader_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, readerId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Reader findReaderById(String readerId) {
        String sql = "SELECT reader_id, name, phone, email, max_borrow_count FROM readers WHERE reader_id = ? AND status = '正常'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, readerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Reader reader = new Reader(
                    rs.getString("reader_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                reader.setMaxBorrowCount(rs.getInt("max_borrow_count"));
                return reader;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Reader> findReadersByName(String name) {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT reader_id, name, phone, email, max_borrow_count FROM readers WHERE name LIKE ? AND status = '正常'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Reader reader = new Reader(
                    rs.getString("reader_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                reader.setMaxBorrowCount(rs.getInt("max_borrow_count"));
                readers.add(reader);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;
    }
}