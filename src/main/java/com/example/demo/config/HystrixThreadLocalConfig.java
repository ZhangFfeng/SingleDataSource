package com.example.demo.config;

import com.example.demo.hystrix.ThreadLocalAwareStrategy;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * 配置SpringCloud以使用自定义的Hystrix并发策略
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Configuration
public class HystrixThreadLocalConfig {
    // 当构造配置对象时，它将自动装配在现有的HystrixConcurrencyStrategy中
    @Autowired(required = false)
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    /**
     * hystrix插件相关内容初始化
     */
    @PostConstruct
    public void init() {
        // 保留现在的Hystrix插件的引用（因为要注册一个新的并发策略，所以要获取所有其他的Hystrix组件，然后重新设置Hystrix插件）
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
        HystrixPlugins.reset();
        // 使用Hystrix插件注册自定义的Hystrix并发策略(ThreadLocalAwareStrategy)--Hystrix只允许一个HystrixConcurrencyStrategy
        HystrixPlugins.getInstance().registerConcurrencyStrategy(new ThreadLocalAwareStrategy(existingConcurrencyStrategy));
        // 然后重新注册Hystrix插件使用的所有的Hystrix组件
        //事件通知
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        //度量信息
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        // properties配置
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        // hystrixcommand回调函数
        HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);

    }
}
