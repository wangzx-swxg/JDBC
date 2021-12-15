package com.hzx.demo02.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils1 {
//    加载驱动
    public static Connection getConnection() throws Exception {
//        1.获取jdbc文件
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        pros.load(is);
//        2.获取jdbc下的内容
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
//        加载驱动
        Class.forName(driverClass);
        //        获取链接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
//    关闭文件
    public static void closeResource(Connection conn, Statement ps) {
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
