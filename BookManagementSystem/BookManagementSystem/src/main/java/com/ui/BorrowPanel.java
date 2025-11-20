package com.ui;

import com.service.BookService;
import com.service.ReaderService;
import com.service.BorrowService;
import javax.swing.*;
import java.awt.*;

public class BorrowPanel extends JPanel {
    private BookService bookService;
    private ReaderService readerService;
    private BorrowService borrowService;
    
    private JTextField readerIdField;
    private JTextField isbnField;
    private JTextArea resultArea;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color SUCCESS_COLOR = new Color(46, 139, 87);
    private final Color WARNING_COLOR = new Color(255, 140, 0);
    
    public BorrowPanel(BookService bookService, ReaderService readerService, BorrowService borrowService) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.borrowService = borrowService;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 248, 255));
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), 
            "📥 图书借阅"
        ));
        
        // 输入面板
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        // 结果显示区域
        JPanel resultPanel = createResultPanel();
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 添加说明面板
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 读者ID输入
        JLabel readerLabel = new JLabel("👤 读者ID:*");
        readerLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        readerIdField = createStyledTextField();
        
        // 图书ISBN输入
        JLabel isbnLabel = new JLabel("📚 图书ISBN:*");
        isbnLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        isbnField = createStyledTextField();
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton borrowButton = createStyledButton("✅ 借书", SUCCESS_COLOR);
        JButton clearButton = createStyledButton("🔄 清空", PRIMARY_COLOR);
        
        borrowButton.addActionListener(e -> borrowBook());
        clearButton.addActionListener(e -> clearFields());
        
        buttonPanel.add(borrowButton);
        buttonPanel.add(clearButton);
        
        // 添加到输入面板
        inputPanel.add(readerLabel);
        inputPanel.add(readerIdField);
        inputPanel.add(isbnLabel);
        inputPanel.add(isbnField);
        inputPanel.add(new JLabel()); // 空标签占位
        inputPanel.add(buttonPanel);
        
        return inputPanel;
    }
    
    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JLabel resultLabel = new JLabel("📋 借书结果:");
        resultLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        resultArea = new JTextArea(8, 50);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(250, 250, 250));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        return resultPanel;
    }
    
    /**
     * 创建信息面板的方法
     * @return 配置好的JPanel面板
     */
    private JPanel createInfoPanel() {
        // 创建一个使用BorderLayout布局的面板
        JPanel infoPanel = new JPanel(new BorderLayout());
        // 设置面板背景色为浅米色
        infoPanel.setBackground(new Color(255, 248, 225));
        // 设置复合边框，外层为金黄色线条边框，内层为空白边距
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // 创建提示信息标签
        JLabel infoLabel = new JLabel("💡 温馨提示: 借书成功后请务必记下借阅记录ID，还书时需要用到此ID");
        // 设置字体为微软雅黑，普通样式，12号大小
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        // 设置文字颜色为深棕色
        infoLabel.setForeground(new Color(139, 69, 19));
        
        // 将标签添加到面板中央
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        
        // 返回配置好的面板
        return infoPanel;
    }
    
    /**
     * 创建一个具有特定样式的文本框
     * @return 返回一个设置了字体、边框样式的JTextField对象
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();  // 创建一个基本的文本框
        // 设置文本框字体为微软雅黑，普通样式，大小为14
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        // 创建复合边框：外层为1像素宽的浅灰色线条边框，内层为8像素的空白内边距
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),  // 外层边框
            BorderFactory.createEmptyBorder(8, 12, 8, 12)  // 修正这里
        ));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void borrowBook() {
        String readerId = readerIdField.getText().trim();
        String isbn = isbnField.getText().trim();
        
        // 输入验证
        if (readerId.isEmpty() || isbn.isEmpty()) {
            showResult("❌ 错误：读者ID和图书ISBN不能为空", false);
            return;
        }
        
        // 检查读者是否存在
        if (readerService.findReaderById(readerId) == null) {
            showResult("❌ 错误：读者ID \"" + readerId + "\" 不存在", false);
            return;
        }
        
        // 检查图书是否存在
        if (bookService.findBookByIsbn(isbn) == null) {
            showResult("❌ 错误：图书ISBN \"" + isbn + "\" 不存在", false);
            return;
        }
        
        try {
            // 执行借书操作
            String result = borrowService.borrowBook(readerId, isbn);
            
            if (result.contains("成功")) {
                // 提取借阅记录ID
                String recordId = extractRecordId(result);
                
                // 构建成功消息
                String successMessage = buildSuccessMessage(result, recordId, readerId, isbn);
                showResult(successMessage, true);
                
                // 清空输入字段
                clearInputFields();
                
                // 显示额外提示对话框
                showRecordIdWarning(recordId);
                
            } else {
                showResult("❌ " + result, false);
            }
            
        } catch (Exception e) {
            showResult("❌ 系统错误: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }
    
    private String extractRecordId(String result) {
        // 从结果中提取记录ID
        if (result.contains("记录ID:")) {
            String[] parts = result.split("记录ID:");
            if (parts.length > 1) {
                return parts[1].split("，")[0].trim();
            }
        }
        return "未知";
    }
    
    private String buildSuccessMessage(String originalResult, String recordId, String readerId, String isbn) {
        StringBuilder message = new StringBuilder();
        message.append("✅ 借书成功！\n\n");
        message.append("📋 借阅详情:\n");
        message.append("───────────────\n");
        message.append("• 读者ID: ").append(readerId).append("\n");
        message.append("• 图书ISBN: ").append(isbn).append("\n");
        message.append("• 借阅记录ID: ").append(recordId).append("\n");
        message.append("• 借书时间: ").append(new java.util.Date()).append("\n\n");
        
        // 添加重要提示
        message.append("⚠️ 重要提醒:\n");
        message.append("───────────────\n");
        message.append("• 请务必记下借阅记录ID: ").append(recordId).append("\n");
        message.append("• 还书时需要提供此ID\n");
        message.append("• 建议截图或拍照保存\n");
        
        return message.toString();
    }
    
    private void showRecordIdWarning(String recordId) {
        // 创建自定义的提示对话框
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "📝 请记录借阅ID", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setResizable(false);
        
        // 图标和标题
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        headerPanel.add(iconLabel);
        dialog.add(headerPanel, BorderLayout.NORTH);
        
        // 内容面板
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(Color.WHITE);
        messageArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        messageArea.setText(
            "✅ 借书成功！\n\n" +
            "📋 您的借阅记录ID是：\n" +
            "────────────────────\n" +
            "   " + recordId + "\n\n" +
            "💡 重要提示：\n" +
            "• 还书时必须提供此ID\n" +
            "• 请妥善保管此号码\n" +
            "• 建议截图或记录在安全的地方"
        );
        
        // 高亮显示记录ID
        messageArea.setSelectionStart(messageArea.getText().indexOf(recordId));
        messageArea.setSelectionEnd(messageArea.getText().indexOf(recordId) + recordId.length());
        messageArea.select(messageArea.getSelectionStart(), messageArea.getSelectionEnd());
        
        contentPanel.add(messageArea, BorderLayout.CENTER);
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton okButton = new JButton("✅ 我已记下");
        okButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        okButton.setBackground(new Color(46, 139, 87));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showResult(String message, boolean isSuccess) {
        SwingUtilities.invokeLater(() -> {
            resultArea.setText(message);
            resultArea.setCaretPosition(0);
            
            // 根据成功与否设置不同的背景色
            if (isSuccess) {
                resultArea.setBackground(new Color(240, 255, 240));
            } else {
                resultArea.setBackground(new Color(255, 240, 240));
            }
        });
    }
    
    private void clearInputFields() {
        readerIdField.setText("");
        isbnField.setText("");
    }
    
    private void clearFields() {
        clearInputFields();
        resultArea.setText("");
        resultArea.setBackground(new Color(250, 250, 250));
    }
}