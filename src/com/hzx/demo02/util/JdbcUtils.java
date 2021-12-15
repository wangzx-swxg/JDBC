package com.hzx.demo02.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {
    //  获取链接
    public static Connection getConnection() throws Exception {
//        1.读取配置文件
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);
//      获取配置文件的内容
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
//        加载驱动
        Class.forName(driverClass);
//        链接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    //  关闭连接和Statement的操作
    public static void closeResource(Connection conn, PreparedStatement ps) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  关闭资源操作
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
