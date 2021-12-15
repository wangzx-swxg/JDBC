package com.hzx.demo02.bean;

import java.security.Timestamp;

public class employee {
    private int id;
    private String name;
    private int age;
    private String position;
    private Timestamp hireTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Timestamp getHireTime() {
        return hireTime;
    }

    public void setHireTime(Timestamp hireTime) {
        this.hireTime = hireTime;
    }

    public employee() {
    }
}
