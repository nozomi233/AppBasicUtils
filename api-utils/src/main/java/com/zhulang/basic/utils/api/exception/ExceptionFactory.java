package com.zhulang.common.common.exception;

import com.zhulang.common.common.alert.api.IResponseCode;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class ExceptionFactory {
    public static BizException bizException(String errorMessage) {
        throw new BizException(errorMessage);
    }

    public static BizException bizException(int errorCode, String errorMessage) {
        throw new BizException(errorCode, errorMessage);
    }

    public static BizException bizException(IResponseCode responseCode) {
        throw new BizException(responseCode);
    }

    public static SysException sysException(IResponseCode responseCode) {
        throw new SysException(responseCode);
    }

    public static SysException sysException(String errorMessage) {
        throw new SysException(errorMessage);
    }

    public static SysException sysException(int errorCode, String errorMessage) {
        throw new SysException(errorCode, errorMessage);
    }

    public static SysException sysException(String errorMessage, Throwable e) {
        throw new SysException(errorMessage, e);
    }

    public static SysException sysException(int errorCode, String errorMessage, Throwable e) {
        throw new SysException(errorCode, errorMessage, e);
    }

    public static ProcessException processException(int errorCode, String errorMessage) {
        throw new ProcessException(errorCode, errorMessage);
    }

    public static ProcessException processException(IResponseCode responseCode) {
        throw new ProcessException(responseCode);
    }

    public static ProcessException processException(String errorMessage) {
        throw new ProcessException(errorMessage);
    }

    public static RpcException rpcException(IResponseCode responseCode) {
        throw new RpcException(responseCode);
    }

    public static RpcException rpcException(String errorMessage) {
        throw new RpcException(errorMessage);
    }

    public static RpcException rpcException(int errorCode, String errorMessage) {
        throw new RpcException(errorCode, errorMessage);
    }

    public static RpcException rpcException(String errorMessage, Throwable e) {
        throw new RpcException(errorMessage, e);
    }

    public static RpcException rpcException(int errorCode, String errorMessage, Throwable e) {
        throw new RpcException(errorCode, errorMessage, e);
    }

    public static DbException dbException(IResponseCode responseCode) {
        throw new DbException(responseCode);
    }

    public static DbException dbException(String errorMessage) {
        throw new DbException(errorMessage);
    }

    public static DbException dbException(int errorCode, String errorMessage) {
        throw new DbException(errorCode, errorMessage);
    }

    public static DbException dbException(String errorMessage, Throwable e) {
        throw new DbException(errorMessage, e);
    }

    public static DbException dbException(int errorCode, String errorMessage, Throwable e) {
        throw new DbException(errorCode, errorMessage, e);
    }
}
