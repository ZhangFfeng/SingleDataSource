package com.example.demo.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * feign降级处理
 *
 * @Author: zhangfeng
 * @Date: 2019/4/24 10:04
 * @Version 1.0
 */
@Component
public class HystrixClientFallback implements OrganzationService {
    Logger logger = LoggerFactory.getLogger(HystrixClientFallback.class);

    @Override
    public String sayHello(String name) {
        logger.info("feign超时配置");
        return "请重试";
    }

    @Override
    public String saySorry(String name) {
        return null;
    }
}
