package com.example.demo.controller;

import com.example.demo.dto.request.BaseRequestDTO;
import com.example.demo.dto.response.BaseResponseDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.OrganzationService;
import com.example.demo.service.TestService;
import com.example.demo.util.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhangfeng
 * @Date: 2019/4/23 13:40
 * @Version 1.0
 */
@RestController
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private TestService testService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrganzationService organzationService;


    @Autowired
    private RedisLock redisLock;

    /**
     * jdbcTemplate测试
     */
    @GetMapping(value = "test1")
    @ApiOperation(value = "jdbcTemplate测试", notes = "jdbcTemplate测试")
    public BaseResponseDTO test1(BaseRequestDTO<User> baseRequestDTO) {
        try {
            testService.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * mybatis测试
     */
    @GetMapping(value = "test2")
    @ApiOperation(value = "mybatis测试", notes = "mybatis测试")
    public void test2() {
        try {
            testService.save2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试死信队列.
     *
     * @param p the p
     * @return the response entity
     */
    @RequestMapping(value = "/dead", method = RequestMethod.GET)
    public BaseResponseDTO deadLetter(String p) {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//        声明消息处理器  这个对消息进行处理  可以设置一些参数   对消息进行一些定制化处理   我们这里  来设置消息的编码  以及消息的过期时间  因为在.net 以及其他版本过期时间不一致   这里的时间毫秒值 为字符串
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
//            设置编码
            messageProperties.setContentEncoding("utf-8");
//            设置过期时间10*1000毫秒
            messageProperties.setExpiration("10000");
            return message;
        };
//         向DL_QUEUE 发送消息  10*1000毫秒后过期 形成死信
        rabbitTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY", p, messagePostProcessor, correlationData);

//        rabbitTemplate.convertAndSend("DIRECT_EXCHANGE", "DIRECT_ROUTING_KEY", p, messagePostProcessor, correlationData);
//        rabbitTemplate.convertAndSend("DIRECT_EXCHANGE", "aa", p, messagePostProcessor, correlationData);
        baseResponseDTO.setSuccess(true);
        return baseResponseDTO;
    }

    /**
     * 测试hystrix父子线程隔离模式传值
     */
    @ApiOperation(value = "舱壁模式 父子线程", notes = "舱壁模式")
    @RequestMapping(value = "/testHystrix2", method = RequestMethod.GET)
    public void testHystrix2() {
        Long l1 = System.currentTimeMillis();
        testService.testHystrix("1");
    }

    /**
     * 测试feign客户端调用
     */
    @ApiOperation(value = "feign客户端调用", notes = "feign客户端调用")
    @RequestMapping(value = "/testFeign", method = RequestMethod.GET)
    public void testFeign() {
        Long l1 = System.currentTimeMillis();
        try {
            System.out.println("feign客户端调用2222");
            String result = organzationService.sayHello("mutiDatasource");
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info(" testFeign{}ms", System.currentTimeMillis() - l1);
    }


    /**
     * 测试redis并发锁
     *
     * @param baseRequestDTO 接收类
     * @return
     */
    @ApiOperation(value = "redislock", notes = "redislock")
    @RequestMapping(value = "/redislock")
    public BaseResponseDTO<User> redislock(BaseRequestDTO<User> baseRequestDTO) {

        boolean q = redisLock.tryLock("lockKey", "123456789", 30, TimeUnit.SECONDS);
        String b = redisLock.get("lockKey");
        redisLock.releaseLock("lockKey", b);
        return null;
    }

}
