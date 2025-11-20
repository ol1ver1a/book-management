# book-management

基于java技术开发的一个图书管理系统，用来交实训课的一个实训任务（包括代码，需求分析，数据表，详细任务书等）

运行说明：先运行DatabaseInitializer.java将数据库安装到本地，然后查看pom.xml中的配置是否需要修改，最后直接运行Main.java即可

本系统基于Java Swing 技术栈开发的桌面应用程序


核心技术栈：

版本：Java 8+ 兼容
基础：面向对象编程(OOP)
特性：异常处理、集合框架、IO流、多线程

Swing GUI 框架：// 主要使用的Swing组件
JFrame, JPanel, JButton, JTextField, JTable
JTabbedPane, JDialog, JScrollPane, JLabel
JTextArea, JMenuBar, JMenuItem

MVC 架构模式：Model (数据模型)      - com.model.*
View (用户界面)       - com.ui.*  
Controller (控制器)   - com.service.* + com.dao.*


具体技术实现：
前端技术 (Swing)：// 界面组件
JFrame - 主窗口
JTabbedPane - 选项卡布局
JTable + DefaultTableModel - 数据表格
JDialog - 弹出对话框
// 事件处理
ActionListener - 按钮点击事件
MouseListener - 鼠标事件
WindowListener - 窗口事件

后端技术：// 数据访问层
PreparedStatement - 防SQL注入
ResultSet - 结果集处理
Transaction - 事务管理
// 业务逻辑层
Service类 - 业务规则验证
数据验证、借阅逻辑、罚款计算

数据存储技术：// SQLite数据库
jdbc:sqlite:library.db
PRAGMA foreign_keys = ON - 外键约束
// 文件备份
ObjectOutputStream - 对象序列化
FileInputStream/FileOutputStream - 文件IO



设计模式应用
DAO模式 - 数据访问对象

MVC模式 - 模型-视图-控制器

Singleton模式 - 数据库连接

Observer模式 - 事件监听
