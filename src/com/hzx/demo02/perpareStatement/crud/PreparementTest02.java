package com.hzx.demo02.perpareStatement.crud;

import com.hzx.demo02.bean.Customer;
import com.hzx.demo02.bean.Order;
import com.hzx.demo02.util.JdbcUtils2;
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

public class PreparementTest02 {

    public static <T> List<T> query(Class<T> clazz,String sql,Object ... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
//                填入占位符
                ps.setObject(i+1,args[i]);
            }
//            执行executeQuery,返回结果集ResultSet
            rs = ps.executeQuery();
//            通过getMetaData获取结果集中的列数
            ResultSetMetaData rsmd = rs.getMetaData();
//            返回结果集中列的个数
            int count = rsmd.getColumnCount();
            List<T> list = new ArrayList<>();
            while (rs.next()) {
//                实例化对象
                T t = clazz.newInstance();
//                循环结果集中列的个数
                for (int i = 0; i < count; i++) {
//                    获取列的值
                    Object columnValue = rs.getObject(i + 1);
//                    获取列的名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
//                    给t对象指定的columnLabel赋值,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
//                    Field field = clazz.getField(columnLabel);
//                    如果有私有对象
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils2.closeResource(conn,ps,rs);
        }
        return null;
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
    @Test
    public void update() throws ParseException {
        String sql = "insert into `order`(order_name,order_date) values(?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = sdf.parse("1008-10-1");
        update(sql, "zs", new java.sql.Date(date.getTime()));
    }
    public static void update(String sql,Object ... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
//                填入占位符
                ps.setObject(i+1,args[i]);
            }
//            执行提交
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils2.closeResource(conn,ps);
        }
    }
}
