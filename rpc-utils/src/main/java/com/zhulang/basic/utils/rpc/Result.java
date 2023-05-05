package com.zhulang.basic.utils.rpc;

import com.alibaba.fastjson.JSON;

/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class Result<T> {

    /**
     * 成功标识
     */
    private boolean success;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJsonString(this);
    }
}
