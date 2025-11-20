package com.service;

import com.model.Reader;
import com.dao.ReaderDAO;
import java.util.List;

public class ReaderService {
    private ReaderDAO readerDAO;
    
    public ReaderService() {
        this.readerDAO = new ReaderDAO();
    }
    
    public boolean registerReader(Reader reader) {
        // 检查读者ID是否已存在
        Reader existingReader = readerDAO.findReaderById(reader.getReaderId());
        if (existingReader != null) {
            return false; // 读者ID已存在
        }
        return readerDAO.registerReader(reader);
    }
    
    public boolean updateReader(Reader updatedReader) {
        // 检查读者是否存在
        Reader existingReader = readerDAO.findReaderById(updatedReader.getReaderId());
        if (existingReader == null) {
            return false; // 读者不存在
        }
        return readerDAO.updateReader(updatedReader);
    }
    
    public boolean deleteReader(String readerId) {
        // 检查读者是否存在
        Reader existingReader = readerDAO.findReaderById(readerId);
        if (existingReader == null) {
            return false; // 读者不存在
        }
        return readerDAO.deleteReader(readerId);
    }
    
    public Reader findReaderById(String readerId) {
        return readerDAO.findReaderById(readerId);
    }
    
    public List<Reader> findReadersByName(String name) {
        return readerDAO.findReadersByName(name);
    }
    
    public List<Reader> getAllReaders() {
        return readerDAO.loadReaders();
    }
    
    public void saveData() {
        // 数据库自动保存，无需手动保存
        System.out.println("读者数据已自动保存到数据库");
    }
    
    /**
     * 检查读者是否可以借书
     * @param readerId 读者ID
     * @return 可以借书返回true，否则返回false
     */
    public boolean canBorrowBooks(String readerId) {
        Reader reader = findReaderById(readerId);
        if (reader == null) {
            return false; // 读者不存在
        }
        
        // 这里可以添加更多检查逻辑，比如读者状态等
        return true;
    }
    
    /**
     * 获取读者的最大借书数量
     * @param readerId 读者ID
     * @return 最大借书数量，如果读者不存在返回0
     */
    public int getMaxBorrowCount(String readerId) {
        Reader reader = findReaderById(readerId);
        if (reader == null) {
            return 0;
        }
        return reader.getMaxBorrowCount();
    }
}