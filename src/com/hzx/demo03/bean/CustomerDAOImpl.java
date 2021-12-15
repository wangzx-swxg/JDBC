package com.hzx.demo03.bean;

import com.hzx.demo03.BaseDao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class CustomerDAOImpl extends BaseDao<Customer> implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer cust) {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int update = update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
        if (update > 0) {
            System.out.println("插入成功!");
        } else {
            System.out.println("失败！");
        }
    }

    @Override
    public void deleteById(Connection conn, int id) {

    }

    @Override
    public void update(Connection conn, Customer cust) {

    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql  = "select id,name,email,birth from customers where id=? ";
        Customer instance = getInstance(conn, sql, id);
        return instance;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers ";
        List<Customer> list = getForList(conn, sql);
        return list;

    }

    @Override
    public Long getCount(Connection conn) {
        return null;
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        return null;
    }
}
