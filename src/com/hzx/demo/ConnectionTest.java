package com.hzx.demo;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    public void test01() throws SQLException {
//        1.提供Driver接口实现类的对象
        Driver driver = new com.mysql.jdbc.Driver();
//      2.提供链接的url地址
        String url = "jdbc:mysql://localhost:3306/test";
//        3.使用Properties链接数据库
        Properties pros = new Properties();
        pros.setProperty("user","root");
        pros.setProperty("password","123456");
//        4.链接
        Connection connect = driver.connect(url, pros);
        System.out.println(connect);
    }

    @Test
    public void test02() throws Exception{
//        使用反射获取链接
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
//        2.url
        String url = "jdbc:mysql://localhost:3306/test";
//      3.
        Properties pros = new Properties();
        pros.setProperty("user","root");
        pros.setProperty("password","123456");
//        4.获取链接
        Connection connect = driver.connect(url, pros);
        System.out.println(connect);
    }
//  推荐使用
    @Test
    public void test03() throws Exception {
//        1.
        Properties pros = new Properties();
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        pros.load(is);
//      2.通过pros获取jdbc下的内容
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
//        3.加载驱动
        Class.forName(driverClass);
//        4.获取链接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

}
