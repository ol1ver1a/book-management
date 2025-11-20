package com.model;

import java.io.Serializable;

public class Reader implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String readerId;
    private String name;
    private String phone;
    private String email;
    private int maxBorrowCount;
    
    public Reader(String readerId, String name, String phone, String email) {
        this.readerId = readerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.maxBorrowCount = 5; // 默认最多借5本书
    }
    
    // Getters and Setters
    public String getReaderId() { return readerId; }
    public void setReaderId(String readerId) { this.readerId = readerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getMaxBorrowCount() { return maxBorrowCount; }
    public void setMaxBorrowCount(int maxBorrowCount) { 
        this.maxBorrowCount = maxBorrowCount; 
    }
    
    @Override
    public String toString() {
        return String.format("读者ID: %s, 姓名: %s, 电话: %s, 邮箱: %s, 最大借书数: %d", 
                readerId, name, phone, email, maxBorrowCount);
    }
}
