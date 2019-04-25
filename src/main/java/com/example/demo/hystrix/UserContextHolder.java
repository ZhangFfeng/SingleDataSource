package com.example.demo.hystrix;

import org.springframework.util.Assert;

/**
 * 管理UserContext数据
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
public class UserContextHolder {
    // 将UserContext存储在一个静态ThreadLocal变量中
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    /**
     * 将检索UserContext以供使用
     *
     * @return 用户数据存储
     */
    public static final UserContext getContext() {
        UserContext context = userContext.get();
        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);
        }
        return userContext.get();
    }

    /**
     * 将UserContext存储至静态ThreadLocal变量
     * @param context 用户数据存储
     */
    public static final void setContext(UserContext context) {
        Assert.notNull(context, "only non-null usercontext instances are permitted");
        userContext.set(context);
    }

    /**
     * 创建一个存储对象UserContext
     * @return
     */
    public static final UserContext createEmptyContext() {
        return new UserContext();
    }
}
