package com.ui;

import com.model.Book;
import com.service.BookService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {
    private BookService bookService;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel statsLabel;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color SUCCESS_COLOR = new Color(46, 139, 87);
    private final Color WARNING_COLOR = new Color(255, 140, 0);
    private final Color DANGER_COLOR = new Color(220, 20, 60);
    private final Color BG_LIGHT = new Color(240, 248, 255);
    
    public BookPanel(BookService bookService) {
        this.bookService = bookService;
        initializeUI();
        loadBooks();
        updateStats();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_LIGHT);
        
        // 创建顶部面板
        add(createTopPanel(), BorderLayout.NORTH);
        // 创建中间表格面板
        add(createTablePanel(), BorderLayout.CENTER);
        // 创建底部统计面板
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BG_LIGHT);
        
        // 搜索面板
        JPanel searchPanel = createSearchPanel();
        topPanel.add(searchPanel, BorderLayout.NORTH);
        
        // 操作按钮面板
        JPanel buttonPanel = createButtonPanel();
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        return topPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), 
            "🔍 搜索图书"
        ));
        
        JLabel searchLabel = new JLabel("搜索条件:");
        searchLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        JButton searchButton = createStyledButton("搜索", PRIMARY_COLOR);
        JButton showAllButton = createStyledButton("显示全部", new Color(100, 149, 237));
        
        searchButton.addActionListener(e -> searchBooks());
        showAllButton.addActionListener(e -> {
            loadBooks();
            searchField.setText("");
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        
        return searchPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), 
            "📚 图书操作"
        ));
        
        JButton addButton = createStyledButton("➕ 添加图书", SUCCESS_COLOR);
        JButton editButton = createStyledButton("✏️ 修改图书", WARNING_COLOR);
        JButton deleteButton = createStyledButton("🗑️ 删除图书", DANGER_COLOR);
        JButton refreshButton = createStyledButton("🔄 刷新", new Color(138, 43, 226));
        
        addButton.addActionListener(e -> showAddBookDialog());
        editButton.addActionListener(e -> showEditBookDialog());
        deleteButton.addActionListener(e -> deleteBook());
        refreshButton.addActionListener(e -> loadBooks());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        return buttonPanel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ISBN", "书名", "作者", "出版社", "价格", "总数", "可借数量", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bookTable.setRowHeight(30);
        bookTable.setSelectionBackground(new Color(220, 240, 255));
        bookTable.setSelectionForeground(Color.BLACK);
        bookTable.setGridColor(new Color(240, 240, 240));
        bookTable.setShowGrid(true);
        bookTable.setIntercellSpacing(new Dimension(1, 1));
        
        // 设置表头
        JTableHeader header = bookTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), 
            "📖 图书列表"
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        statsLabel.setForeground(PRIMARY_COLOR);
        
        bottomPanel.add(statsLabel);
        
        return bottomPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
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
    
    private void loadBooks() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                tableModel.setRowCount(0);
                List<Book> books = bookService.getAllBooks();
                for (Book book : books) {
                    String status = book.getAvailableQuantity() > 0 ? "🟢 可借" : "🔴 已借完";
                    Object[] rowData = {
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        String.format("¥%.2f", book.getPrice()),
                        book.getTotalQuantity(),
                        book.getAvailableQuantity(),
                        status
                    };
                    tableModel.addRow(rowData);
                }
                return null;
            }
            
            @Override
            protected void done() {
                updateStats();
            }
        };
        worker.execute();
    }
    
    /**
     * 更新统计信息的方法
     * 计算图书总数、可借数量和已借数量，并在界面上显示
     */
    private void updateStats() {
        // 获取所有图书的数量作为总数
        int totalBooks = bookService.getAllBooks().size();
        // 使用流式处理计算可借阅的图书数量
        long availableBooks = bookService.getAllBooks().stream()
                // 过滤出可借阅数量大于0的图书
                .filter(book -> book.getAvailableQuantity() > 0)
                // 统计符合条件的图书数量
                .count();
        // 计算已借出的图书数量（总数减去可借数量）
        long borrowedBooks = totalBooks - availableBooks;
        
        // 设置统计标签的文本，显示图书总数、可借数量和已借数量
        statsLabel.setText(String.format(
            "📊 统计信息: 总计 %d 本图书 | 🟢 可借 %d 本 | 🔴 已借 %d 本", 
            totalBooks, availableBooks, borrowedBooks
        ));
    }
    
    /**
     * 搜索图书的方法
     * 根据输入的关键词在书名和作者中搜索匹配的图书
     * 并在表格中显示搜索结果
     */
    private void searchBooks() {
        // 获取搜索框中的关键词并去除前后空格
        String keyword = searchField.getText().trim();
        // 如果关键词为空，则加载所有图书并返回
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }
        
        // 清空表格模型中的所有行
        tableModel.setRowCount(0);
        // 根据书名搜索图书
        List<Book> books = bookService.findBooksByTitle(keyword);
        // 根据作者搜索图书，并将结果添加到列表中
        books.addAll(bookService.findBooksByAuthor(keyword));
        
        // 遍历搜索到的图书列表
        for (Book book : books) {
            // 根据可借数量设置状态显示
            String status = book.getAvailableQuantity() > 0 ? "🟢 可借" : "🔴 已借完";
            // 创建表格行数据对象数组
            Object[] rowData = {
                book.getIsbn(),          // ISBN号
                book.getTitle(),         // 书名
                book.getAuthor(),
                book.getPublisher(),     // 出版社
                String.format("¥%.2f", book.getPrice()),  // 价格，格式化为两位小数
                book.getTotalQuantity(), // 总数量
                book.getAvailableQuantity(), // 可借数量
                status                  // 状态
            };
            // 将行数据添加到表格模型
            tableModel.addRow(rowData);
        }
        updateStats();
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "未找到包含 \"" + keyword + "\" 的图书", 
                "搜索结果", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * 显示添加新图书的对话框
     * 该方法创建一个包含图书信息输入字段的对话框，并处理保存和取消操作
     */
    private void showAddBookDialog() {
        // 创建一个带有标题"添加新图书"的对话框，设置宽度和高度
        JDialog dialog = createStyledDialog("➕ 添加新图书", 500, 450);
        // 创建一个使用GridBagLayout的内容面板，设置白色背景和内边距
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建GridBagConstraints对象，用于设置组件的布局约束
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // 组件水平填充
        gbc.insets = new Insets(5, 5, 10, 5); // 设置组件之间的间距
        
        // ISBN输入字段
        gbc.gridx = 0; gbc.gridy = 0; // 设置网格位置
        contentPanel.add(createStyledLabel("ISBN:*"), gbc); // 添加标签
        gbc.gridx = 1; // 移动到下一列
        JTextField isbnField = createStyledTextField(); // 创建文本输入框
        contentPanel.add(isbnField, gbc); // 添加文本输入框
        
        // 书名输入字段
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createStyledLabel("书名:*"), gbc);
        gbc.gridx = 1;
        JTextField titleField = createStyledTextField();
        contentPanel.add(titleField, gbc);
        
        // 作者输入字段
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createStyledLabel("作者:"), gbc);
        gbc.gridx = 1;
        JTextField authorField = createStyledTextField();
        contentPanel.add(authorField, gbc);
        
        // 出版社输入字段
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createStyledLabel("出版社:"), gbc);
        gbc.gridx = 1;
        JTextField publisherField = createStyledTextField();
        contentPanel.add(publisherField, gbc);
        
        // 价格输入字段
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createStyledLabel("价格:*"), gbc);
        gbc.gridx = 1;
        JTextField priceField = createStyledTextField();
        contentPanel.add(priceField, gbc);
        
        // 总数输入字段
        gbc.gridx = 0; gbc.gridy = 5;
        contentPanel.add(createStyledLabel("总数:*"), gbc);
        gbc.gridx = 1;
        JTextField quantityField = createStyledTextField();
        contentPanel.add(quantityField, gbc);
        
        // 按钮面板
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2; // 跨两列
        gbc.anchor = GridBagConstraints.CENTER; // 居中对齐
        // 创建按钮面板，使用流式布局
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        // 创建保存和取消按钮
        JButton saveButton = createStyledButton("💾 保存", SUCCESS_COLOR);
        JButton cancelButton = createStyledButton("❌ 取消", DANGER_COLOR);
        
        buttonPanel.add(saveButton); // 添加保存按钮
        buttonPanel.add(cancelButton); // 添加取消按钮
        contentPanel.add(buttonPanel, gbc); // 将按钮面板添加到内容面板
        
        dialog.add(contentPanel); // 将内容面板添加到对话框
        
        // 事件处理
        saveButton.addActionListener(e -> {
            try {
                String isbn = isbnField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String publisher = publisherField.getText().trim();
                String priceText = priceField.getText().trim();
                String quantityText = quantityField.getText().trim();
                
                // 验证输入
                if (isbn.isEmpty() || title.isEmpty()) {
                    showErrorDialog(dialog, "ISBN和书名不能为空");
                    return;
                }
                
                if (priceText.isEmpty() || quantityText.isEmpty()) {
                    showErrorDialog(dialog, "价格和总数不能为空");
                    return;
                }
                
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);
                
                if (price < 0 || quantity < 1) {
                    showErrorDialog(dialog, "价格不能为负数，总数必须大于0");
                    return;
                }
                
                Book book = new Book(isbn, title, author, publisher, price, quantity);
                if (bookService.addBook(book)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ 图书添加成功！\n\nISBN: " + isbn + "\n书名: " + title, 
                        "添加成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadBooks();
                    dialog.dispose();
                } else {
                    showErrorDialog(dialog, "ISBN \"" + isbn + "\" 已存在！");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog(dialog, "价格和总数必须为有效数字");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    /**
     * 显示编辑图书信息的对话框
     * 该方法会创建一个包含图书信息编辑表单的对话框
     * 用户可以修改图书的各个属性，并保存修改
     */
    private void showEditBookDialog() {
        // 获取当前在表格中选中的行索引
        int selectedRow = bookTable.getSelectedRow();
        // 检查是否选中了行
        if (selectedRow == -1) {
            // 如果没有选中行，显示警告提示用户先选择要修改的图书
            JOptionPane.showMessageDialog(this, 
                "⚠️ 请先选择要修改的图书", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return; // 直接返回，不执行后续操作
        }
        
        // 获取选中行的ISBN值
        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        // 根据ISBN查找图书
        Book book = bookService.findBookByIsbn(isbn);
        // 检查图书是否存在
        if (book == null) {
            // 如果图书不存在，显示错误信息
            JOptionPane.showMessageDialog(this, 
                "❌ 图书不存在", 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            return; // 直接返回，不执行后续操作
        }
        
        // 创建一个自定义样式的对话框
        JDialog dialog = createStyledDialog("✏️ 修改图书信息", 500, 450);
        // 创建内容面板，使用网格布局管理器
        JPanel contentPanel = new JPanel(new GridBagLayout());
        // 设置面板背景色和边距
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建网格约束对象，用于控制组件在网格中的位置和大小
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // 组件水平填充
        gbc.insets = new Insets(5, 5, 10, 5); // 组件间距
        
        // ISBN (不可编辑)
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createStyledLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        JTextField isbnField = createStyledTextField();
        isbnField.setText(book.getIsbn());
        isbnField.setEditable(false);
        isbnField.setBackground(new Color(240, 240, 240));
        contentPanel.add(isbnField, gbc);
        
        // 书名
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createStyledLabel("书名:*"), gbc);
        gbc.gridx = 1;
        JTextField titleField = createStyledTextField();
        titleField.setText(book.getTitle());
        contentPanel.add(titleField, gbc);
        
        // 作者
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createStyledLabel("作者:"), gbc);
        gbc.gridx = 1;
        JTextField authorField = createStyledTextField();
        authorField.setText(book.getAuthor());
        contentPanel.add(authorField, gbc);
        
        // 出版社
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createStyledLabel("出版社:"), gbc);
        gbc.gridx = 1;
        JTextField publisherField = createStyledTextField();
        publisherField.setText(book.getPublisher());
        contentPanel.add(publisherField, gbc);
        
        // 价格
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createStyledLabel("价格:*"), gbc);
        gbc.gridx = 1;
        JTextField priceField = createStyledTextField();
        priceField.setText(String.valueOf(book.getPrice()));
        contentPanel.add(priceField, gbc);
        
        // 总数
        gbc.gridx = 0; gbc.gridy = 5;
        contentPanel.add(createStyledLabel("总数:*"), gbc);
        gbc.gridx = 1;
        JTextField quantityField = createStyledTextField();
        quantityField.setText(String.valueOf(book.getTotalQuantity()));
        contentPanel.add(quantityField, gbc);
        
        // 按钮面板
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("💾 保存修改", SUCCESS_COLOR);
        JButton cancelButton = createStyledButton("❌ 取消", DANGER_COLOR);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, gbc);
        
        dialog.add(contentPanel);
        
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String publisher = publisherField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                
                if (title.isEmpty()) {
                    showErrorDialog(dialog, "书名不能为空");
                    return;
                }
                
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublisher(publisher);
                book.setPrice(price);
                book.setTotalQuantity(quantity);
                
                if (bookService.updateBook(book)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ 图书信息修改成功！", 
                        "修改成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadBooks();
                    dialog.dispose();
                } else {
                    showErrorDialog(dialog, "修改失败");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog(dialog, "价格和总数必须为有效数字");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ 请先选择要删除的图书", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "⚠️ 确定要删除以下图书吗？\n\nISBN: " + isbn + "\n书名: " + title + "\n\n此操作不可撤销！", 
            "确认删除", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            if (bookService.deleteBook(isbn)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ 图书删除成功！", 
                    "删除成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ 删除失败", 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JDialog createStyledDialog(String title, int width, int height) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setResizable(false);
        return dialog;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, 12));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }
    
    private void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            "❌ " + message, 
            "输入错误", 
            JOptionPane.ERROR_MESSAGE);
    }
}