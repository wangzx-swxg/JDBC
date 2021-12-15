package com.hzx.demo03;

import com.hzx.demo02.util.JdbcUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> {
    private Class<T> clazz = null;
    public BaseDao() {
//        获取当前BaseDao的子类继承的父类中的泛性
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) genericSuperclass;
        Type[] types = parameterized.getActualTypeArguments();
        clazz = (Class<T>) types[0];//泛型的第一个参数
    }

//      增删改
    public int update(Connection conn,String sql,Object ... args) {
        PreparedStatement ps = null;
        try {
//            预编译sql
            ps = conn.prepareStatement(sql);
//            循环遍历，输入内容的个数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
//            执行 execute()
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(null,ps);
        }
        return 0;
    }
//    查:返回一条数据
    public T getInstance(Connection conn,String sql,Object ... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //            预编译sql
        try {
            ps = conn.prepareStatement(sql);
            //            循环遍历，输入内容的个数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
//            执行execuQuery,返回结果集
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
//            获取元数据的个数
            int count = rsmd.getColumnCount();
//            判断rs结果集是否还有下一个
            if (rs.next()) {
//                实例对象
                T t = clazz.newInstance();
//                处理结果集中的每一列
                for (int i = 0; i < count; i++) {
//              获取值
                    Object columnValue = rs.getObject(i + 1);
//                    获取列
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
//                    如果有私有对象
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }

                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(null,ps,rs);
        }
        return null;
    }
//    通用查询
    public List<T> getForList(Connection conn,String sql,Object ... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //            预编译sql
        try {
            ps = conn.prepareStatement(sql);
            //            循环遍历，输入内容的个数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            List<T> list  = new ArrayList<>();
            int count = rsmd.getColumnCount();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < count; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

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
            JdbcUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    //用于查询特殊值的通用的方法
    public <E> E getValue(Connection conn,String sql,Object...args) {
        PreparedStatement ps = null;
        ResultSet rs  = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.closeResource(null, ps, rs);

        }
        return null;
    }
}
