package com.zhulang.basic.utils.common.exception;

/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class DependencyException extends BaseException {

    public DependencyException(Integer code, String message) {
        super(code, message);
    }

    public DependencyException(IErrorCode errorCode, Object... fillMessage) {
        super(errorCode.getErrorCode(), String.format(errorCode.getErrorMsg(), fillMessage));
    }

    public DependencyException(IErrorCode errorCode) {
        super(errorCode);
    }
}

