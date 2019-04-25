package com.example.demo.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据传输对象接收体
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@ApiModel(description = "接口接收外层参数")
public class BaseRequestDTO<T> {
    @ApiModelProperty("返回时间，毫秒值")
    private Long time;
    @ApiModelProperty("返回业务数据体")
    private T data;

    public BaseRequestDTO() {
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}