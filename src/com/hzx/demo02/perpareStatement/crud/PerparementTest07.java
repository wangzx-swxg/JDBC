package com.hzx.demo02.perpareStatement.crud;

import com.hzx.demo02.util.JdbcUtils;
import com.hzx.demo04.util.JDBCUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class PerparementTest07 {
    @Test
    public void xxx() throws Exception {
//获取连接
        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

// 填充占位符
        ps.setString(1, "徐海强");
        ps.setString(2, "xhq@126.com");
        ps.setDate(3, new Date(new java.util.Date().getTime()));
// 操作Blob类型的变量
        FileInputStream fis = new FileInputStream("成功.png");
        ps.setBlob(4, fis);
//执行
        ps.execute();

        fis.close();
        JdbcUtils.closeResource(conn,ps);
    }

    @Test
    public void testInsert2() throws Exception{
        long start = System.currentTimeMillis();

        Connection conn = JDBCUtils.getConnection();

        //1.设置为不自动提交数据
        conn.setAutoCommit(false);

        String sql = "INSERT INTO `mysql`.`employee` (`name`, `age`, `position`, `hire_time`) VALUES ( ?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(int i = 1;i <= 4000000;i++){
            ps.setString(1, "name_" + i);
            ps.setInt(2, 18);
            ps.setString(3,"员工");
            ps.setDate(4,new Date(new java.util.Date().getTime()));
            //1.“攒”sql
            ps.addBatch();

            if(i % 500 == 0){
                //2.执行
                ps.executeBatch();
                //3.清空
                ps.clearBatch();
            }
        }

        //2.提交数据
        conn.commit();

        long end = System.currentTimeMillis();
        System.out.println("花费的时间为：" + (end - start));//1000000条:4978

        JdbcUtils.closeResource(conn,ps);
    }
}
