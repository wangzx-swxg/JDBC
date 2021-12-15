package com.hzx.demo02.perpareStatement.crud;

import com.hzx.demo02.bean.Customer;
import com.hzx.demo02.bean.Order;
import com.hzx.demo02.util.JdbcUtils;
import com.hzx.demo02.util.JdbcUtils1;
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

public class PreparementUpdateTest01 {
    @Test
    public void query() {
        String sql = "select id,name,email,birth from customers where id = ?";
        List<Customer> list = query(Customer.class, sql, 5);
        list.forEach(System.out::println);

        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order`";
        List<Order> orderList = query(Order.class, sql1);
        orderList.forEach(System.out::println);

    }
    @Test
    public void update() throws ParseException {
        String sql = "insert into `order`(order_name,order_date) values(?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = sdf.parse("2001-10-1");
        update(sql, "zs", new java.sql.Date(date.getTime()));
    }
    public <T> List<T> query(Class<T> clazz,String sql,Object ... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
//            获取链接
            conn = JdbcUtils.getConnection();
//            执行sql语句
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
//                填入占位符
                ps.setObject(i+1,args[i]);
            }
//            执行executeQuery,获取数据源ResultSet
            rs = ps.executeQuery();
//            获取元数据
            ResultSetMetaData rsmd = rs.getMetaData();
//            获取所有数据数
            int count = rsmd.getColumnCount();
            List<T> list  =new ArrayList<>();
            while (rs.next()) {
//            获取实例的对象
                T t = clazz.newInstance();
                //                处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < count; i++) {
//                    获取列的值
                    Object columnValue = rs.getObject(i + 1);
//                    获取列的名称
                    String columnLabel = rsmd.getColumnLabel(i+1);
//                    给t对象知道你过的columnName属性，赋值威columnValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
//                    获取私有属性
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils1.closeResource(conn,ps,rs);
        }
        return null;
    }
    public static void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
//            1.获取链接
            conn = JdbcUtils1.getConnection();
//            放入sql语句
            ps = conn.prepareStatement(sql);
//          循环遍历插入 字符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
//            执行提交
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils1.closeResource(conn,ps);
        }
    }
}
