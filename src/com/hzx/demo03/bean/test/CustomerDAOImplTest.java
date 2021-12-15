package com.hzx.demo03.bean.test;

import com.hzx.demo02.util.JdbcUtils;
import com.hzx.demo03.bean.Customer;
import com.hzx.demo03.bean.CustomerDAO;
import com.hzx.demo03.bean.CustomerDAOImpl;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImplTest {
    private CustomerDAO dao = new CustomerDAOImpl();

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            Customer cust = new Customer(1, "于小飞", "xiaofei@126.com",new Date(43534646435L));
            dao.insert(conn,cust);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(conn,null);
        }
    }

    @Test
    public void queryByID() {
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 12);
            System.out.println(customer);
//            List<Customer> all = dao.getAll(conn);
//            all.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void query() {
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            List<Customer> all = dao.getAll(conn);
            all.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
