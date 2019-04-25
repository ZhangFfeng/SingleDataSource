package com.example.demo.hystrix;

import java.util.concurrent.Callable;

/**
 * Hystrix自定义隔离策略（将Usercontext注入Hystrix命令中）
 *
 * @param <V> 任意类型值
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
public final class DelegatingUserContextCallable<V> implements Callable<V> {
    private final Callable<V> delegate;
    private UserContext originalUserContext;

    /**
     * 原始Callable类奖杯传递到自定医德callable类，自定义Callable将调用Hystrix保护 的代码和来自父线程的UserContext
     * @param delegate
     * @param context
     */
    public DelegatingUserContextCallable(Callable<V> delegate, UserContext context) {
        this.delegate = delegate;
        this.originalUserContext = context;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     * call()方法在被@HystrixCommand注解保护的方法前调用
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public V call() throws Exception {
        // 已设置Usercontext。存储UserContext的Threadlocal变量与运行收Hystrix保护的方法的线程相关联
        UserContextHolder.setContext(originalUserContext);
        try {
            // UserContext设置之后，在Hystrix保护的方法上调用call()方法
            return delegate.call();
        } finally {
            this.originalUserContext = null;
        }

    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext context) {
        return new DelegatingUserContextCallable<>(delegate, context);
    }
}
