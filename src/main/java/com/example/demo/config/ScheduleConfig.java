package com.example.demo.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.swing.plaf.metal.MetalTheme;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 定时器配置文件
 * 串行改并行
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Configuration
public class ScheduleConfig implements SchedulingConfigurer, AsyncConfigurer {

    private Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    /**
     * 并行任务
     *
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        TaskScheduler taskScheduler = taskScheduler();
        taskRegistrar.setTaskScheduler(taskScheduler);

    }


    /**
     * 并行任务使用策略：多线程处理（配置线程数等）
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("MyTaskScheduler-");  //设置线程名开头
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }


    /**
     * 异步任务
     *
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor1 = new ThreadPoolTaskExecutor();
        executor1.setCorePoolSize(10);
        executor1.setThreadNamePrefix("AsyncExecutor");
        executor1.initialize();
        return executor1;
    }


    /**
     * 异步任务 异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                logger.error("======================="+throwable.getLocalizedMessage()
                        +"=======================",throwable);
                logger.error("exception method : " + method.getName());
            }
        };
    }
}