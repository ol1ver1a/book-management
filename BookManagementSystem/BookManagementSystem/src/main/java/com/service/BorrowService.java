package com.service;

import com.model.Book;
import com.model.Reader;
import com.model.BorrowRecord;
import com.dao.BorrowRecordDAO;
import com.dao.BookDAO;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BorrowService {
    private BorrowRecordDAO borrowRecordDAO;
    private BookService bookService;
    private ReaderService readerService;
    
    public BorrowService(BookService bookService, ReaderService readerService) {
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.bookService = bookService;
        this.readerService = readerService;
    }
    
    public String borrowBook(String readerId, String isbn) {
        Reader reader = readerService.findReaderById(readerId);
        Book book = bookService.findBookByIsbn(isbn);
        
        if (reader == null) {
            return "错误：读者不存在";
        }
        
        if (book == null) {
            return "错误：图书不存在";
        }
        
        if (book.getAvailableQuantity() <= 0) {
            return "错误：图书已全部借出";
        }
        
        // 检查读者是否已达到最大借书数量
        int currentBorrowCount = borrowRecordDAO.getCurrentBorrowCount(readerId);
        int maxBorrowCount = reader.getMaxBorrowCount();
        
        if (currentBorrowCount >= maxBorrowCount) {
            return String.format("错误：已达到最大借书数量（当前已借：%d本，最大可借：%d本）", 
                currentBorrowCount, maxBorrowCount);
        }
        
        // 检查读者是否已经借了这本书且未归还
        if (borrowRecordDAO.isBookBorrowedByReader(readerId, isbn)) {
            return "错误：您已经借了这本书且未归还";
        }
        
        // 创建借阅记录
        String recordId = generateRecordId();
        BorrowRecord record = new BorrowRecord(recordId, readerId, isbn, new Date());
        
        // 保存借阅记录到数据库
        if (borrowRecordDAO.addBorrowRecord(record)) {
            // 更新图书可借数量（数据库触发器会自动处理）
            // 这里可以添加额外的业务逻辑
            
            return String.format("借书成功！记录ID: %s，书名: %s，应还日期: %tF", 
                recordId, book.getTitle(), record.getDueDate());
        } else {
            return "错误：借书失败，请重试";
        }
    }
    
    public String returnBook(String recordId) {
        BorrowRecord record = borrowRecordDAO.findRecordById(recordId);
        if (record == null) {
            return "错误：借阅记录不存在";
        }
        
        if (record.getReturnDate() != null) {
            return "错误：该书已经归还";
        }
        
        // 设置归还日期
        record.setReturnDate(new Date());
        
        // 计算罚款
        double fine = record.calculateFine();
        record.setFine(fine);
        
        // 更新借阅记录到数据库
        if (borrowRecordDAO.updateBorrowRecord(record)) {
            // 更新图书可借数量（数据库触发器会自动处理）
            
            if (fine > 0) {
                return String.format("还书成功，产生逾期罚款: %.2f元", fine);
            } else {
                return "还书成功";
            }
        } else {
            return "错误：还书失败，请重试";
        }
    }
    
    private String generateRecordId() {
        // 格式：BR + 年月日 + 3位随机数
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomPart = String.format("%03d", new Random().nextInt(1000));
        return "BR" + datePart + randomPart;
    }
    
    public List<BorrowRecord> getBorrowRecordsByReader(String readerId) {
        return borrowRecordDAO.findRecordsByReader(readerId);
    }
    
    public List<BorrowRecord> getBorrowRecordsByBook(String isbn) {
        List<BorrowRecord> allRecords = borrowRecordDAO.loadRecords();
        return allRecords.stream()
                .filter(record -> record.getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }
    
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordDAO.loadRecords();
    }
    
    public List<BorrowRecord> getCurrentBorrowRecords() {
        List<BorrowRecord> allRecords = borrowRecordDAO.loadRecords();
        return allRecords.stream()
                .filter(record -> record.getReturnDate() == null)
                .collect(Collectors.toList());
    }
    
    public List<BorrowRecord> getOverdueRecords() {
        List<BorrowRecord> allRecords = borrowRecordDAO.loadRecords();
        Date now = new Date();
        return allRecords.stream()
                .filter(record -> record.getReturnDate() == null && now.after(record.getDueDate()))
                .collect(Collectors.toList());
    }
    
    public List<BorrowRecord> getReturnedRecords() {
        List<BorrowRecord> allRecords = borrowRecordDAO.loadRecords();
        return allRecords.stream()
                .filter(record -> record.getReturnDate() != null)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取读者的当前借阅数量
     * @param readerId 读者ID
     * @return 当前借阅数量
     */
    public int getReaderCurrentBorrowCount(String readerId) {
        return borrowRecordDAO.getCurrentBorrowCount(readerId);
    }
    
    /**
     * 检查读者是否借阅了某本书且未归还
     * @param readerId 读者ID
     * @param isbn 图书ISBN
     * @return 如果借阅了且未归还返回true
     */
    public boolean isBookBorrowedByReader(String readerId, String isbn) {
        return borrowRecordDAO.isBookBorrowedByReader(readerId, isbn);
    }
    
    /**
     * 获取借阅记录的详细信息（包含图书和读者信息）
     * @param recordId 记录ID
     * @return 包含详细信息的字符串
     */
    public String getBorrowRecordDetails(String recordId) {
        BorrowRecord record = borrowRecordDAO.findRecordById(recordId);
        if (record == null) {
            return "记录不存在";
        }
        
        Book book = bookService.findBookByIsbn(record.getIsbn());
        Reader reader = readerService.findReaderById(record.getReaderId());
        
        StringBuilder details = new StringBuilder();
        details.append("借阅记录详情：\n");
        details.append("记录ID：").append(record.getRecordId()).append("\n");
        details.append("读者：").append(reader != null ? reader.getName() : "未知").append(" (").append(record.getReaderId()).append(")\n");
        details.append("图书：").append(book != null ? book.getTitle() : "未知").append(" (").append(record.getIsbn()).append(")\n");
        details.append("借书日期：").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getBorrowDate())).append("\n");
        details.append("应还日期：").append(new SimpleDateFormat("yyyy-MM-dd").format(record.getDueDate())).append("\n");
        
        if (record.getReturnDate() != null) {
            details.append("还书日期：").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getReturnDate())).append("\n");
            details.append("罚款金额：").append(String.format("%.2f元", record.getFine())).append("\n");
            details.append("状态：已归还");
        } else {
            Date now = new Date();
            if (now.after(record.getDueDate())) {
                long overdueDays = (now.getTime() - record.getDueDate().getTime()) / (24 * 60 * 60 * 1000);
                details.append("逾期天数：").append(overdueDays).append("天\n");
                details.append("预计罚款：").append(String.format("%.2f元", record.calculateFine())).append("\n");
                details.append("状态：逾期");
            } else {
                details.append("状态：借阅中");
            }
        }
        
        return details.toString();
    }
}