package com.example.demo.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign重试机制配置文件
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Configuration
public class FeignConfigure {

    //自定义重试次数
    @Bean
    public Retryer feignRetryer() {
        //重试间隔为100ms，最大重试时间为1s,重试次数为4次
        Retryer retryer = new Retryer.Default(100, 1000, 4);
        return retryer;
    }
}
