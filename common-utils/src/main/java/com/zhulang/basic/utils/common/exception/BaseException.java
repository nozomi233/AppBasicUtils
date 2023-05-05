package com.zhulang.basic.utils.common.exception;

/**
 * 基础异常类
 * @Author zhulang
 * @Date 2023-04-24
 **/
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1388509180752135451L;

    private int errCode;

    public BaseException(String errMessage) {
        super(errMessage);
    }

    public BaseException(int errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public BaseException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public BaseException(int errCode, String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = errCode;
    }

    public BaseException(IErrorCode errorCode) {
        super(errorCode.getErrorMsg());

        this.errCode = errorCode.getErrorCode();
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

}
