package com.ui;

import com.service.BorrowService;
import com.model.BorrowRecord;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReturnPanel extends JPanel {
    private BorrowService borrowService;
    private JTextField recordIdField;
    private JTextArea resultArea;
    private JTable recordTable;
    private DefaultTableModel tableModel;
    
    public ReturnPanel(BorrowService borrowService) {
        this.borrowService = borrowService;
        initializeUI();
        loadBorrowRecords();
    }
    
    /**
     * 初始化用户界面方法
     * 设置界面布局、边距、背景色，并添加顶部面板和中间表格面板
     */
    private void initializeUI() {
        // 设置边界布局，组件之间的水平和垂直间距为10像素
        setLayout(new BorderLayout(10, 10));
        // 设置组件边距，上下左右均为15像素
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        // 设置背景色为淡蓝色
        setBackground(new Color(240, 248, 255));
        
        // 顶部面板 - 还书操作
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // 中间面板 - 借阅记录表格
        JScrollPane tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "📤 还书操作"));
        
        // 输入面板
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        
        inputPanel.add(new JLabel("借阅记录ID:"));
        recordIdField = new JTextField(20);
        recordIdField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputPanel.add(recordIdField);
        
        JButton returnButton = createStyledButton("✅ 还书", new Color(46, 139, 87));
        JButton clearButton = createStyledButton("🔄 清空", new Color(100, 149, 237));
        
        returnButton.addActionListener(e -> returnBook());
        clearButton.addActionListener(e -> clearFields());
        
        inputPanel.add(returnButton);
        inputPanel.add(clearButton);
        
        // 结果显示区域
        resultArea = new JTextArea(3, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(250, 250, 250));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        
        topPanel.add(inputPanel);
        topPanel.add(resultScrollPane);
        
        return topPanel;
    }
    
    /**
     * 创建一个包含借阅记录表格的滚动面板
     * @return JScrollPane 配置好的滚动面板，包含借阅记录表格
     */
    private JScrollPane createTablePanel() {
        // 定义表格列名
        String[] columnNames = {"记录ID", "读者ID", "ISBN", "书名", "借书日期", "应还日期", "状态"};
        // 创建表格模型，并设置为单元格不可编辑
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 创建表格实例并设置基本样式
        recordTable = new JTable(tableModel);
        recordTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));  // 设置表格字体
        recordTable.setRowHeight(25);  // 设置行高
        recordTable.setSelectionBackground(new Color(220, 240, 255));  // 设置选中行的背景色
        
        // 设置表头样式
        JTableHeader header = recordTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 13));  // 设置表头字体
        header.setBackground(new Color(70, 130, 180));  // 设置表头背景色
        header.setForeground(Color.WHITE);  // 设置表头文字颜色
        
        // 创建滚动面板并设置边框
        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "📋 当前借阅记录"));
        
        // 添加表格选择事件监听器
        recordTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = recordTable.getSelectedRow();
                if (selectedRow != -1) {
                    String recordId = (String) tableModel.getValueAt(selectedRow, 0);
                    recordIdField.setText(recordId);
                    resultArea.setText("已选择记录: " + recordId + "\n点击\"还书\"按钮进行归还");
                }
            }
        });
        
        return scrollPane;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker()),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        return button;
    }
    
    private void loadBorrowRecords() {
        tableModel.setRowCount(0);
        List<BorrowRecord> records = borrowService.getAllBorrowRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (BorrowRecord record : records) {
            // 只显示未归还的记录
            if (record.getReturnDate() == null) {
                String status = new Date().after(record.getDueDate()) ? "🔴 逾期" : "🟡 借阅中";
                Object[] rowData = {
                    record.getRecordId(),
                    record.getReaderId(),
                    record.getIsbn(),
                    getBookTitle(record.getIsbn()), // 需要添加这个方法
                    sdf.format(record.getBorrowDate()),
                    sdf.format(record.getDueDate()),
                    status
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private String getBookTitle(String isbn) {
        // 这里需要从BookService获取书名
        // 暂时返回空字符串，您需要根据您的实现来完善
        return "";
    }
    
    private void returnBook() {
        String recordId = recordIdField.getText().trim();
        
        if (recordId.isEmpty()) {
            resultArea.setText("❌ 错误：请输入或选择借阅记录ID");
            return;
        }
        
        String result = borrowService.returnBook(recordId);
        resultArea.setText("📋 还书结果:\n" + result);
        
        if (result.contains("成功")) {
            recordIdField.setText("");
            loadBorrowRecords(); // 刷新表格
        }
    }
    
    private void clearFields() {
        recordIdField.setText("");
        resultArea.setText("");
    }
}