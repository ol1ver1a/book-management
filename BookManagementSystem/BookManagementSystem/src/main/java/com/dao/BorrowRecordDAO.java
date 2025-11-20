package com.dao;

import com.model.BorrowRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAO {
    
    public void saveRecords(List<BorrowRecord> records) {
        // 不再使用文件存储
    }
    
    public List<BorrowRecord> loadRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT record_id, reader_id, book_isbn, borrow_date, due_date, return_date, fine_amount FROM borrow_records";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getString("record_id"),
                    rs.getString("reader_id"),
                    rs.getString("book_isbn"),
                    rs.getTimestamp("borrow_date")
                );
                record.setDueDate(rs.getTimestamp("due_date"));
                record.setReturnDate(rs.getTimestamp("return_date"));
                record.setFine(rs.getDouble("fine_amount"));
                records.add(record);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    public boolean addBorrowRecord(BorrowRecord record) {
        String sql = "INSERT INTO borrow_records (record_id, reader_id, book_isbn, borrow_date, due_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getRecordId());
            pstmt.setString(2, record.getReaderId());
            pstmt.setString(3, record.getIsbn());
            pstmt.setTimestamp(4, new Timestamp(record.getBorrowDate().getTime()));
            pstmt.setTimestamp(5, new Timestamp(record.getDueDate().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateBorrowRecord(BorrowRecord record) {
        String sql = "UPDATE borrow_records SET return_date = ?, fine_amount = ? WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (record.getReturnDate() != null) {
                pstmt.setTimestamp(1, new Timestamp(record.getReturnDate().getTime()));
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }
            pstmt.setDouble(2, record.getFine());
            pstmt.setString(3, record.getRecordId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public BorrowRecord findRecordById(String recordId) {
        String sql = "SELECT record_id, reader_id, book_isbn, borrow_date, due_date, return_date, fine_amount FROM borrow_records WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getString("record_id"),
                    rs.getString("reader_id"),
                    rs.getString("book_isbn"),
                    rs.getTimestamp("borrow_date")
                );
                record.setDueDate(rs.getTimestamp("due_date"));
                record.setReturnDate(rs.getTimestamp("return_date"));
                record.setFine(rs.getDouble("fine_amount"));
                return record;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<BorrowRecord> findRecordsByReader(String readerId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT record_id, reader_id, book_isbn, borrow_date, due_date, return_date, fine_amount FROM borrow_records WHERE reader_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, readerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getString("record_id"),
                    rs.getString("reader_id"),
                    rs.getString("book_isbn"),
                    rs.getTimestamp("borrow_date")
                );
                record.setDueDate(rs.getTimestamp("due_date"));
                record.setReturnDate(rs.getTimestamp("return_date"));
                record.setFine(rs.getDouble("fine_amount"));
                records.add(record);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    public int getCurrentBorrowCount(String readerId) {
        String sql = "SELECT COUNT(*) as count FROM borrow_records WHERE reader_id = ? AND return_date IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, readerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean isBookBorrowedByReader(String readerId, String isbn) {
        String sql = "SELECT COUNT(*) as count FROM borrow_records WHERE reader_id = ? AND book_isbn = ? AND return_date IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, readerId);
            pstmt.setString(2, isbn);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}