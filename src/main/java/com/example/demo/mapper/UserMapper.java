package com.example.demo.mapper;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * mabatis接口类
 *
 * @Author: zhangfeng
 * @Date: 2019/4/23 14:03
 * @Version 1.0
 */
@Service
@Mapper
public interface UserMapper {
    /**
     * 查询all
     *
     * @return
     */
    List<User> queryAllUsers();

    /**
     * 添加
     */
    void saveUser(User user);
}