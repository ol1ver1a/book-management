package com.ui;

import com.service.BookService;
import com.service.ReaderService;
import com.service.BorrowService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private BookService bookService;
    private ReaderService readerService;
    private BorrowService borrowService;
    
    private JTabbedPane tabbedPane;
    private BookPanel bookPanel;
    private ReaderPanel readerPanel;
    private BorrowPanel borrowPanel;
    private ReturnPanel returnPanel;
    
    public MainFrame() {
        initializeServices();
        initializeUI();
    }
    
    private void initializeServices() {
        bookService = new BookService();
        readerService = new ReaderService();
        borrowService = new BorrowService(bookService, readerService);
    }
    
    private void initializeUI() {
        setTitle("📚 图书管理系统 - 管理员");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // 设置应用图标
        try {
            setIconImage(new ImageIcon("icon.png").getImage());
        } catch (Exception e) {
            // 图标文件不存在，忽略
        }
        
        // 添加菜单栏
        createMenuBar();
        
        // 创建主面板带背景
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(230, 240, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        // 创建各个功能面板
        bookPanel = new BookPanel(bookService);
        readerPanel = new ReaderPanel(readerService);
        borrowPanel = new BorrowPanel(bookService, readerService, borrowService);
        returnPanel = new ReturnPanel(borrowService);
        
        // 添加选项卡
        tabbedPane.addTab("📖 图书管理", bookPanel);
        tabbedPane.addTab("👥 读者管理", readerPanel);
        tabbedPane.addTab("📥 借书功能", borrowPanel);
        tabbedPane.addTab("📤 还书功能", returnPanel);
        
        // 设置选项卡样式
        tabbedPane.setBackground(new Color(70, 130, 180));
        tabbedPane.setForeground(Color.WHITE);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // 添加头部横幅
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // 添加状态栏
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 添加窗口关闭监听器，保存数据
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                bookService.saveData();
                readerService.saveData();
                JOptionPane.showMessageDialog(MainFrame.this, 
                    "数据已保存！", 
                    "系统提示", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    // 添加创建菜单栏的方法
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(70, 130, 180));
        menuBar.setForeground(Color.WHITE);
        
        // 系统菜单
        JMenu systemMenu = new JMenu("⚙️ 系统");
        systemMenu.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        systemMenu.setForeground(Color.WHITE);
        
        // 用户信息菜单项
        JMenuItem userItem = new JMenuItem("👤 当前用户: admin");
        userItem.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        userItem.setEnabled(false); // 不可点击
        
        // 退出登录菜单项
        JMenuItem logoutItem = new JMenuItem("🚪 退出登录");
        logoutItem.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        logoutItem.addActionListener(e -> logout());
        
        // 退出系统菜单项
        JMenuItem exitItem = new JMenuItem("❌ 退出系统");
        exitItem.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        exitItem.addActionListener(e -> exitSystem());
        
        systemMenu.add(userItem);
        systemMenu.addSeparator();
        systemMenu.add(logoutItem);
        systemMenu.add(exitItem);
        
        menuBar.add(systemMenu);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("❓ 帮助");
        helpMenu.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        helpMenu.setForeground(Color.WHITE);
        
        JMenuItem aboutItem = new JMenuItem("ℹ️ 关于系统");
        aboutItem.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    // 退出登录方法
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this,
            "确定要退出登录吗？",
            "确认退出",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // 保存数据
            bookService.saveData();
            readerService.saveData();
            
            this.dispose();
            // 重新显示登录界面
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }
    
    // 退出系统方法
    private void exitSystem() {
        int result = JOptionPane.showConfirmDialog(this,
            "确定要退出系统吗？",
            "确认退出",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // 保存数据
            bookService.saveData();
            readerService.saveData();
            System.exit(0);
        }
    }
    
    // 显示关于信息
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "📚 图书管理系统 v1.0\n\n" +
            "功能特色：\n" +
            "• 完整的图书管理功能\n" +
            "• 读者信息管理\n" +
            "• 图书借阅与归还\n" +
            "• 数据持久化存储\n" +
            "• 安全的登录验证\n\n" +
            "开发技术：Java Swing\n" +
            "架构设计：MVC模式\n" +
            "数据存储：文件序列化\n\n" +
            "© 2023 图书管理系统",
            "关于系统",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // 标题
        JLabel titleLabel = new JLabel("📚 图书管理系统", JLabel.LEFT);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        // 副标题
        JLabel subtitleLabel = new JLabel("欢迎使用 - 管理员模式", JLabel.RIGHT);
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(subtitleLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(240, 240, 240));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel statusLabel = new JLabel("就绪 | 登录用户: admin | 系统运行正常");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(100, 100, 100));
        
        // 更新时间显示
        updateTimeLabel(timeLabel);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(timeLabel, BorderLayout.EAST);
        
        return statusPanel;
    }
    
    private void updateTimeLabel(JLabel timeLabel) {
        Timer timer = new Timer(1000, e -> {
            String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            timeLabel.setText("系统时间: " + time);
        });
        timer.start();
    }
    
    // 修改 main 方法，移除直接启动（现在通过登录界面启动）
    public static void main(String[] args) {
        // 这个方法现在不应该被直接调用
        // 系统通过 LoginFrame 启动
        JOptionPane.showMessageDialog(null,
            "请通过登录界面启动系统",
            "启动提示",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void setUIFont(Font font) {
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