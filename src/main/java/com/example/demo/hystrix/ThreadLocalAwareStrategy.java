package com.example.demo.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Hystrix自定义并发策略类（用于处理spring安全信息的传播）
 * 简单来讲---将现有的并发策略作为新并发策略的成员变量
 *
 * @Author: 张丰
 * @Version 1.0
 */
public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    /**
     * 构造方法
     *
     * @param hystrixConcurrencyStrategy 自定义并发策略
     */
    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy hystrixConcurrencyStrategy) {
        // SpringCloud已经定义了一个并发类。将已存在的并发策略传入自定义的HystrixConcurrencyStrategyde 的类构造器中
        this.existingConcurrencyStrategy = hystrixConcurrencyStrategy;

    }

    /**
     * 新并发策略的线程池
     * 在隔离方法上可以自由设置 threadPoolKey 和 threadPoolProperties 参数
     *
     * @param threadPoolKey        线程池的名字的前缀，默认前缀是 hystrix
     * @param threadPoolProperties 线程池的默认参数
     * @return 新并发策略的线程池
     */
    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        return super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    /**
     * 获得线程池的阻塞队列
     *
     * @param maxQueueSize 最大配对长度
     * @return 线程池的阻塞队列
     */
    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {

        return existingConcurrencyStrategy != null ?
                existingConcurrencyStrategy.getBlockingQueue(maxQueueSize) : super.getBlockingQueue(maxQueueSize);
    }

    /**
     * 自定义实现/行为的注入，定制化装饰，即提供在方法被执行前进行装饰的机会，可以用来复制线程状态等附加行为。
     * 解释： 自定义扩展类实现Callable接口，并传入当前Callable变量delegate，
     * 在delegate执行call方法前后进行线程上线文的操作即可实现线程状态在父线程与子线程间的传播。
     *
     * @param callable 包装对象
     * @param <T>      泛型
     * @return 包装对象
     */
    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        // 注入 Callable实现，它将设置UserContext，用来将UserContext从执行用户Rest服务调用的父线程设置为保护正在进行工作方法的Hystrix命令线程
        return existingConcurrencyStrategy != null ?
                new DelegatingUserContextCallable(callable, UserContextHolder.getContext())
                : super.wrapCallable(new DelegatingUserContextCallable(callable, UserContextHolder.getContext()));
    }

    /**
     * Factory method to return an implementation of {@link HystrixRequestVariable} that behaves like a {@link ThreadLocal} except that it
     * is scoped to a request instead of a thread.
     * <p>
     * For example, if a request starts with an HTTP request and ends with the HTTP response, then {@link HystrixRequestVariable} should
     * be initialized at the beginning, available on any and all threads spawned during the request and then cleaned up once the HTTP request is completed.
     * <p>
     * If this method is implemented it is generally necessary to also implemented {@link #wrapCallable(Callable)} in order to copy state
     * from parent to child thread.
     *
     * @param rv {@link HystrixRequestVariableLifecycle} with lifecycle implementations from Hystrix
     * @return {@code HystrixRequestVariable<T>}
     */
    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return super.getRequestVariable(rv);
    }
}
