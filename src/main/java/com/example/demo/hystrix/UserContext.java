package com.example.demo.hystrix;

/**
 * 前台headers请求参数示例
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
public class UserContext {
    // 相关id
    private String correlationId;
    // 用户id
    private String userId;
    // 用户token
    private String authToken;
    // 组织id
    private String orgId;

    /**
     * 构造函数
     */
    public UserContext() {
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
