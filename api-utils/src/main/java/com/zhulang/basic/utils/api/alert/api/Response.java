package com.zhulang.common.common.alert.api;

import com.zhulang.common.common.enums.ResponseCodeEnum;

import java.io.Serializable;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 136030743879688550L;
    private int code;
    private String msg;
    private T data;

    public Response() {
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    @Override
    public String toString() {
        return "response [ code=" + code + ", data=" + data + ", message=" + msg + "]";
    }

    public static <T> Response<T> buildSuccess(T data) {
        Response<T> response = new Response<T>();
        response.setData(data);
        response.setCode(0);
        response.setMsg("请求成功");
        return response;
    }

    public static <T> Response<T> buildSuccess(T data,String msg) {
        Response<T> response = new Response<T>();
        response.setData(data);
        response.setCode(0);
        response.setMsg(msg);
        return response;
    }

    public static <T> Response<T> buildFailure(int code, String msg) {
        Response<T> response = new Response<T>();
        response.setData(null);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static <T> Response<T> buildFailure(int code, String msg, T data) {
        Response<T> response = new Response<T>();
        response.setData(data);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static <T> Response<T> buildFailure(ResponseCodeEnum responseCodeEnum) {
        Response<T> response = new Response<T>();
        response.setData(null);
        response.setCode(responseCodeEnum.getCode());
        response.setMsg(responseCodeEnum.getMsg());
        return response;
    }
}

