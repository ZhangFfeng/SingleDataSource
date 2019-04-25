package com.example.demo.pojo;

import java.io.Serializable;

/**
 * User实体类
 *
 * @Author: zhangfeng
 * @Date: 2019/4/23 14:03
 * @Version 1.0
 */
public class User implements Serializable {
    // 序列化是为了rabbitmq转换时用到
    private static final long serialVersionUID = -3946734305303957850L;
    private int id;
    private String name;

    public User() {
    }

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
}
