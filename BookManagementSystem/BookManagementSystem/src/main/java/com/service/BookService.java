package com.service;

import com.model.Book;
import com.dao.BookDAO;
import java.util.List;
import java.util.ArrayList;

public class BookService {
    private BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAO();
    }
    
    // 所有方法都直接调用DAO层，不再维护本地列表
    public boolean addBook(Book book) {
        return bookDAO.addBook(book);
    }
    
    public boolean deleteBook(String isbn) {
        return bookDAO.deleteBook(isbn);
    }
    
    public boolean updateBook(Book updatedBook) {
        return bookDAO.updateBook(updatedBook);
    }
    
    public Book findBookByIsbn(String isbn) {
        return bookDAO.findBookByIsbn(isbn);
    }
    
    public List<Book> findBooksByTitle(String title) {
        return bookDAO.findBooksByTitle(title);
    }
    
    public List<Book> findBooksByAuthor(String author) {
        return bookDAO.findBooksByAuthor(author);
    }
    
    public List<Book> getAllBooks() {
        return bookDAO.loadBooks();
    }
    
    public void saveData() {
        // 数据库自动保存，无需手动保存
    }
}