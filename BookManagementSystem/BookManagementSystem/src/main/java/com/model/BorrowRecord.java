package com.model;



import java.io.Serializable;
import java.util.Date;

public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String recordId;
    private String readerId;
    private String isbn;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private double fine;
    
    public BorrowRecord(String recordId, String readerId, String isbn, Date borrowDate) {
        this.recordId = recordId;
        this.readerId = readerId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        // 借书期限为30天
        this.dueDate = new Date(borrowDate.getTime() + 30L * 24 * 60 * 60 * 1000);
        this.returnDate = null;
        this.fine = 0.0;
    }
    
    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    
    public String getReaderId() { return readerId; }
    public void setReaderId(String readerId) { this.readerId = readerId; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
    
    // 计算逾期罚款
    /**
     * 计算罚款金额的方法
     * @return 返回罚款金额，如果未逾期则返回0
     */
    public double calculateFine() {
        // 如果归还日期为空，说明图书尚未归还
        if (returnDate == null) {
            // 获取当前日期
            Date now = new Date();
            // 检查当前日期是否超过了应还日期
            if (now.after(dueDate)) {
                // 计算逾期天数
                long overdueDays = (now.getTime() - dueDate.getTime()) / (24 * 60 * 60 * 1000);
                return overdueDays * 0.5; // 每天0.5元罚款
            }
        } else if (returnDate.after(dueDate)) {
            long overdueDays = (returnDate.getTime() - dueDate.getTime()) / (24 * 60 * 60 * 1000);
            return overdueDays * 0.5;
        }
        return 0.0;
    }
}