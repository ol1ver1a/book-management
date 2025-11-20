package com.ui;

import com.model.Reader;
import com.service.ReaderService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ReaderPanel extends JPanel {
    private ReaderService readerService;
    private JTable readerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel statsLabel;
    
    // 颜色方案
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color SUCCESS_COLOR = new Color(46, 139, 87);
    private final Color WARNING_COLOR = new Color(255, 140, 0);
    private final Color DANGER_COLOR = new Color(220, 20, 60);
    private final Color INFO_COLOR = new Color(138, 43, 226);
    private final Color BG_LIGHT = new Color(240, 248, 255);
    
    public ReaderPanel(ReaderService readerService) {
        this.readerService = readerService;
        initializeUI();
        loadReaders();
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
            "🔍 搜索读者"
        ));
        
        JLabel searchLabel = new JLabel("搜索条件:");
        searchLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        searchLabel.setForeground(new Color(80, 80, 80));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchField.setToolTipText("输入读者姓名进行搜索");
        
        JButton searchButton = createStyledButton("🔍 搜索", PRIMARY_COLOR);
        JButton showAllButton = createStyledButton("🔄 显示全部", new Color(100, 149, 237));
        
        searchButton.addActionListener(e -> searchReaders());
        showAllButton.addActionListener(e -> {
            loadReaders();
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
            "👥 读者管理"
        ));
        
        JButton addButton = createStyledButton("➕ 注册读者", SUCCESS_COLOR);
        JButton editButton = createStyledButton("✏️ 修改信息", WARNING_COLOR);
        JButton deleteButton = createStyledButton("🗑️ 注销读者", DANGER_COLOR);
        JButton refreshButton = createStyledButton("🔄 刷新", INFO_COLOR);
        
        addButton.addActionListener(e -> showAddReaderDialog());
        editButton.addActionListener(e -> showEditReaderDialog());
        deleteButton.addActionListener(e -> deleteReader());
        refreshButton.addActionListener(e -> loadReaders());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        return buttonPanel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columnNames = {"读者ID", "姓名", "电话", "邮箱", "最大借书数", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        readerTable = new JTable(tableModel);
        readerTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        readerTable.setRowHeight(32);
        readerTable.setSelectionBackground(new Color(220, 240, 255));
        readerTable.setSelectionForeground(Color.BLACK);
        readerTable.setGridColor(new Color(240, 240, 240));
        readerTable.setShowGrid(true);
        readerTable.setIntercellSpacing(new Dimension(1, 1));
        
        // 设置表头
        JTableHeader header = readerTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(readerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), 
            "📋 读者列表"
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
    
    private void loadReaders() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                tableModel.setRowCount(0);
                List<Reader> readers = readerService.getAllReaders();
                for (Reader reader : readers) {
                    String status = "🟢 正常";
                    Object[] rowData = {
                        reader.getReaderId(),
                        reader.getName(),
                        formatPhone(reader.getPhone()),
                        reader.getEmail(),
                        reader.getMaxBorrowCount() + " 本",
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
    
    private String formatPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "未填写";
        }
        return phone;
    }
    
    private void updateStats() {
        int totalReaders = readerService.getAllReaders().size();
        statsLabel.setText(String.format(
            "📊 统计信息: 总计 %d 位注册读者", 
            totalReaders
        ));
    }
    
    private void searchReaders() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadReaders();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Reader> readers = readerService.findReadersByName(keyword);
        
        for (Reader reader : readers) {
            String status = "🟢 正常";
            Object[] rowData = {
                reader.getReaderId(),
                reader.getName(),
                formatPhone(reader.getPhone()),
                reader.getEmail(),
                reader.getMaxBorrowCount() + " 本",
                status
            };
            tableModel.addRow(rowData);
        }
        updateStats();
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "🔍 未找到包含 \"" + keyword + "\" 的读者", 
                "搜索结果", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAddReaderDialog() {
        JDialog dialog = createStyledDialog("➕ 注册新读者", 500, 400);
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 12, 8);
        
        // 读者ID
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createStyledLabel("读者ID:*"), gbc);
        gbc.gridx = 1;
        JTextField idField = createStyledTextField();
        idField.setToolTipText("请输入唯一的读者标识ID");
        contentPanel.add(idField, gbc);
        
        // 姓名
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createStyledLabel("姓名:*"), gbc);
        gbc.gridx = 1;
        JTextField nameField = createStyledTextField();
        contentPanel.add(nameField, gbc);
        
        // 电话
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createStyledLabel("电话:"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = createStyledTextField();
        phoneField.setToolTipText("请输入联系电话（可选）");
        contentPanel.add(phoneField, gbc);
        
        // 邮箱
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createStyledLabel("邮箱:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = createStyledTextField();
        emailField.setToolTipText("请输入电子邮箱（可选）");
        contentPanel.add(emailField, gbc);
        
        // 最大借书数
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createStyledLabel("最大借书数:*"), gbc);
        gbc.gridx = 1;
        JTextField maxBorrowField = createStyledTextField();
        maxBorrowField.setText("5");
        maxBorrowField.setToolTipText("设置读者最多可借阅的图书数量");
        contentPanel.add(maxBorrowField, gbc);
        
        // 按钮面板
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("💾 保存注册", SUCCESS_COLOR);
        JButton cancelButton = createStyledButton("❌ 取消", DANGER_COLOR);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, gbc);
        
        dialog.add(contentPanel);
        
        // 事件处理
        saveButton.addActionListener(e -> {
            try {
                String readerId = idField.getText().trim();
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String maxBorrowText = maxBorrowField.getText().trim();
                
                // 验证输入
                if (readerId.isEmpty() || name.isEmpty()) {
                    showErrorDialog(dialog, "读者ID和姓名不能为空");
                    return;
                }
                
                if (maxBorrowText.isEmpty()) {
                    showErrorDialog(dialog, "最大借书数不能为空");
                    return;
                }
                
                int maxBorrow = Integer.parseInt(maxBorrowText);
                if (maxBorrow < 1 || maxBorrow > 20) {
                    showErrorDialog(dialog, "最大借书数必须在1-20之间");
                    return;
                }
                
                Reader reader = new Reader(readerId, name, phone, email);
                reader.setMaxBorrowCount(maxBorrow);
                
                if (readerService.registerReader(reader)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ 读者注册成功！\n\n读者ID: " + readerId + "\n姓名: " + name + "\n最大借书数: " + maxBorrow + "本", 
                        "注册成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadReaders();
                    dialog.dispose();
                } else {
                    showErrorDialog(dialog, "读者ID \"" + readerId + "\" 已存在！");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog(dialog, "最大借书数必须为有效整数");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void showEditReaderDialog() {
        int selectedRow = readerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ 请先选择要修改的读者", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String readerId = (String) tableModel.getValueAt(selectedRow, 0);
        Reader reader = readerService.findReaderById(readerId);
        if (reader == null) {
            JOptionPane.showMessageDialog(this, 
                "❌ 读者不存在", 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = createStyledDialog("✏️ 修改读者信息", 500, 400);
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 12, 8);
        
        // 读者ID (不可编辑)
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createStyledLabel("读者ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = createStyledTextField();
        idField.setText(reader.getReaderId());
        idField.setEditable(false);
        idField.setBackground(new Color(245, 245, 245));
        contentPanel.add(idField, gbc);
        
        // 姓名
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createStyledLabel("姓名:*"), gbc);
        gbc.gridx = 1;
        JTextField nameField = createStyledTextField();
        nameField.setText(reader.getName());
        contentPanel.add(nameField, gbc);
        
        // 电话
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createStyledLabel("电话:"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = createStyledTextField();
        phoneField.setText(reader.getPhone());
        contentPanel.add(phoneField, gbc);
        
        // 邮箱
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createStyledLabel("邮箱:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = createStyledTextField();
        emailField.setText(reader.getEmail());
        contentPanel.add(emailField, gbc);
        
        // 最大借书数
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createStyledLabel("最大借书数:*"), gbc);
        gbc.gridx = 1;
        JTextField maxBorrowField = createStyledTextField();
        maxBorrowField.setText(String.valueOf(reader.getMaxBorrowCount()));
        contentPanel.add(maxBorrowField, gbc);
        
        // 按钮面板
        gbc.gridx = 0; gbc.gridy = 5;
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
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                int maxBorrow = Integer.parseInt(maxBorrowField.getText().trim());
                
                if (name.isEmpty()) {
                    showErrorDialog(dialog, "姓名不能为空");
                    return;
                }
                
                if (maxBorrow < 1 || maxBorrow > 20) {
                    showErrorDialog(dialog, "最大借书数必须在1-20之间");
                    return;
                }
                
                reader.setName(name);
                reader.setPhone(phone);
                reader.setEmail(email);
                reader.setMaxBorrowCount(maxBorrow);
                
                if (readerService.updateReader(reader)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ 读者信息修改成功！", 
                        "修改成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadReaders();
                    dialog.dispose();
                } else {
                    showErrorDialog(dialog, "修改失败");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog(dialog, "最大借书数必须为有效整数");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void deleteReader() {
        int selectedRow = readerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ 请先选择要注销的读者", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String readerId = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "⚠️ 确定要注销以下读者吗？\n\n读者ID: " + readerId + "\n姓名: " + name + "\n\n此操作不可撤销！", 
            "确认注销", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            if (readerService.deleteReader(readerId)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ 读者注销成功！", 
                    "注销成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadReaders();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ 注销失败", 
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