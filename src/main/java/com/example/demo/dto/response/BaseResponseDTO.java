package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据传输对象返回体
 *
 * @Author: 张丰
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@JsonIgnoreProperties(
    ignoreUnknown = true
)
@ApiModel(
    description = "接口返回外层参数"
)
public class BaseResponseDTO<T> {
    @ApiModelProperty("返回时间，毫秒值")
    private Long time;
    @ApiModelProperty("返回状态代码 0：操作失败，1：操作成功")
    private boolean success = true;
    @ApiModelProperty("返回业务代码")
    private int code;
    @ApiModelProperty("返回业务消息")
    private String msg;
    @ApiModelProperty("返回业务数据体")
    private T data;

    public BaseResponseDTO() {
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static BaseResponseDTO success() {
        return getResult(true, 0, "成功", (Object)null);
    }

    public static BaseResponseDTO exception(Exception e) {
        return getResult(false, -1, "系统异常" + e.getMessage(), (Object)null);
    }

    public static BaseResponseDTO fail(String errorMsg) {
        return getResult(false, -1, errorMsg, (Object)null);
    }

    public static BaseResponseDTO successtData(Object data) {
        return getResult(true, 0, "成功", data);
    }

    private static BaseResponseDTO getResult(boolean success, int errorCode, String errorMsg, Object data) {
        BaseResponseDTO result = new BaseResponseDTO();
        result.setTime(System.currentTimeMillis());
        result.setSuccess(success);
        result.setCode(errorCode);
        result.setMsg(errorMsg);
        result.setData(data);
        return result;
    }
}