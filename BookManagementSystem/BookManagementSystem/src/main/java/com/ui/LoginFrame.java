package com.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    // 预定义的用户名和密码（实际项目中应该从数据库读取）
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123456";
    
    public LoginFrame() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("📚 图书管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // 设置窗口图标
        setIconImage(new ImageIcon("icon.png").getImage());
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 头部面板
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // 登录表单面板
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // 底部按钮面板
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        
        // 图标
        JLabel iconLabel = new JLabel("📚", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 标题
        JLabel titleLabel = new JLabel("图书管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        // 副标题
        JLabel subtitleLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        headerPanel.add(iconLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 15, 10);
        
        // 用户名标签
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("👤 用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        formPanel.add(userLabel, gbc);
        
        // 用户名输入框
        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(usernameField, gbc);
        
        // 密码标签
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("🔒 密码:");
        passLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        formPanel.add(passLabel, gbc);
        
        // 密码输入框
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(passwordField, gbc);
        
        // 提示信息
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel hintLabel = new JLabel("提示: 用户名: admin 密码: 123456");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        hintLabel.setForeground(new Color(150, 150, 150));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        formPanel.add(hintLabel, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // 登录按钮
        JButton loginButton = new JButton("🚪 登录系统");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginButton.setBackground(new Color(46, 139, 87));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87).darker(), 1),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 退出按钮
        JButton exitButton = new JButton("❌ 退出程序");
        exitButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 20, 60).darker(), 1),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 添加鼠标悬停效果
        addButtonHoverEffect(loginButton, new Color(46, 139, 87));
        addButtonHoverEffect(exitButton, new Color(220, 20, 60));
        
        // 添加事件监听
        loginButton.addActionListener(new LoginAction());
        exitButton.addActionListener(e -> System.exit(0));
        
        // 回车键登录
        getRootPane().setDefaultButton(loginButton);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        return buttonPanel;
    }
    
    private void addButtonHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }
    
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            // 输入验证
            if (username.isEmpty() || password.isEmpty()) {
                showError("请输入用户名和密码");
                return;
            }
            
            // 验证登录信息
            if (authenticate(username, password)) {
                loginSuccess();
            } else {
                showError("用户名或密码错误");
                passwordField.setText("");
                usernameField.requestFocus();
            }
        }
    }
    
    private boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
    
    private void loginSuccess() {
        // 显示成功消息
        JOptionPane.showMessageDialog(this,
            "✅ 登录成功！\n欢迎使用图书管理系统",
            "登录成功",
            JOptionPane.INFORMATION_MESSAGE);
        
        // 关闭登录窗口，打开主系统
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            "❌ " + message,
            "登录失败",
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统外观
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                
                // 设置全局字体
                setUIFont(new Font("微软雅黑", Font.PLAIN, 13));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
    
    // 设置全局字体
    private static void setUIFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }
}