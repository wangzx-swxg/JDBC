package com.hzx.demo;

import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class StatementTest {
    //    使用statement
    @Test
    public void test01() {
        Scanner scan = new Scanner(System.in);
        System.out.print("用户名：");
        String userName = scan.nextLine();
        System.out.print("密 码：");
        String password = scan.nextLine();

//        String sql = "select * from user where name='1' and password='1' or '1'='1' ";
        String sql = "SELECT user,password FROM user_table WHERE USER = '" + userName + "' AND PASSWORD = '" + password
                + "'";
        User user = get(sql, User.class);
        if (user!= null) {
            System.out.println("success");
        } else {
            System.out.println("Fail");
        }
    }

    //    使用Statement实现对数据库表的操作
    public <T> T get(String sql, Class<T> clazz) {
        T t = null;

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
//            1.加载文件
            Properties pros = new Properties();
//            通过反射获取当前目录下的 jdbc
            InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
//            读取流文件里的内容
            pros.load(is);
            // 2.读取配置信息
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

//            3.链接数据库
            conn = DriverManager.getConnection(url, user, password);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
//            获取结果集的元数据
            ResultSetMetaData data = rs.getMetaData();
//            获取结果集
            int count = data.getColumnCount();
            if(rs.next()) {
                t = clazz.newInstance();
                for (int i = 0; i < count; i++) {
                    // //1. 获取列的名称
                    // String columnName = rsmd.getColumnName(i+1);

                    // 1. 获取列的别名
                    String columnName = data.getColumnLabel(i + 1);

                    // 2. 根据列名获取对应数据表中的数据
                    Object columnVal = rs.getObject(columnName);

                    // 3. 将数据表中得到的数据，封装进对象
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
