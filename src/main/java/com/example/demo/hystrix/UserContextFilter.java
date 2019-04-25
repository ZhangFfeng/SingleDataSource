package com.example.demo.hystrix;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 解析Http首部（Headers）并检索数据
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Component
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
       // 检索调用额HTTP首部中设置的值，将这些值赋给存储在UserContextHolder中的UserContext
        UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader("correlationId"));
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader("userId"));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader("authToken"));
        UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader("orgId"));
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
