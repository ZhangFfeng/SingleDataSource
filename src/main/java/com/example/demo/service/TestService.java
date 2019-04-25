package com.example.demo.service;

import com.example.demo.hystrix.UserContextHolder;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: zhangfeng
 * @Date: 2019/4/23 13:45
 * @Version 1.0
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class TestService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMapper userMapper;

    /**
     * jdbcTemplate 测试
     * 插入
     *
     * @throws Exception
     */
    public void save() throws Exception {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{1, "小明"});
        list.add(new Object[]{2, "小明"});

        String sql = "insert into user(id,name) values(?,?)";
        System.out.println(jdbcTemplate.batchUpdate(sql, list));

    }


    /**
     * mybatis测试
     * 插入
     *
     * @throws Exception
     */
    public void save2() throws Exception {
        User user = new User();
        user.setId(ThreadLocalRandom.current().nextInt());
        user.setName(ThreadLocalRandom.current().nextDouble() + "");
        userMapper.saveUser(user);

    }

    // 舱壁模式处理测试~~单独线程获取不到父线程的thread中的值
    @HystrixCommand(fallbackMethod = "buildFallbackMethod2",
            threadPoolKey = "testThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            })
    public void testHystrix(String id) {
        // 父线程（隔离线程） 获得的数据correlationId
        System.out.println("****service**" + UserContextHolder.getContext().getCorrelationId());
        System.out.println(100 / 0);
    }

    //后备处理方法必须与源方法参数保持一致--降级处理
    public void buildFallbackMethod2(String id) {
        System.out.println("buildFallbackMethods断路器---后备测试");
    }
}
