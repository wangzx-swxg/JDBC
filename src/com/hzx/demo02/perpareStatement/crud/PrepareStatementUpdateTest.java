package com.hzx.demo02.perpareStatement.crud;

import com.hzx.demo02.bean.Customer;
import com.hzx.demo02.bean.Order;
import com.hzx.demo02.util.JdbcUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrepareStatementUpdateTest {

    @Test
    public void update() throws ParseException {
//        String sql = "delete from `order` where order_id=?";
        String sql = "insert into `order`(order_name,order_date) values(?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = sdf.parse("2001-10-1");
        update(sql, "zs", new java.sql.Date(date.getTime()));
    }

    @Test
    public void query() throws Exception {
        String sql = "select id,name,email,birth from customers where id = ?";
        List<Customer> list = query(Customer.class, sql, 5);
        list.forEach(System.out::println);

        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order`";
        List<Order> orderList = query(Order.class, sql1);
        orderList.forEach(System.out::println);
    }
    //    查询
    public <T> List<T> query(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //        1.获取链接
            conn = JdbcUtils.getConnection();
//           2.预编译sql语句，得到PreparStatement对象
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
//            3.填入占位符，写入查询的内容
                ps.setObject(i + 1, args[i]);
            }
//            4.执行executeQuery（），得到结果集 ResultSet
            rs = ps.executeQuery();
//            获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
//            通过ResultSetMetaData获取结果集中的列数
            int count = rsmd.getColumnCount();
//            创建集合对象
            List<T> list = new ArrayList<>();
//            只要还有查询的还有结果集
            while (rs.next()) {
//                获取实例对象
                T t = clazz.newInstance();
//                处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < count; i++) {
//                    获取列的值
                    Object columnValue = rs.getObject(i+1);
//                    获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            关闭资源
            JdbcUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    //    增删改
    public void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
//            1.获取数据库链接
            conn = JdbcUtils.getConnection();
//            2.获取PrepareStatement的实例
            ps = conn.prepareStatement(sql);
//            3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
//            执行sql语句
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            关闭资源
            JdbcUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void updateTest() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
//            1.链接数据库
            conn = JdbcUtils.getConnection();
//            2.写入sql语句
            String sql = "update `customers` set name=? where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, "老树皮");
            ps.setObject(2, 3);
//           执行sql语句
            boolean b = ps.execute();
            if (b) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void testInsert() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
//        1.链接数据库
        conn = JdbcUtils.getConnection();
//        2.写入语句
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        ps = conn.prepareStatement(sql);
        //3.填充占位符
        ps.setString(1, "金刚葫芦娃");
        ps.setString(2, "xxxx@qq.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("1000-01-01");
        ps.setDate(3, new java.sql.Date(date.getTime()));
//      4.执行
        ps.execute();
//       5.关闭资源
        JdbcUtils.closeResource(conn, ps);
    }
}
