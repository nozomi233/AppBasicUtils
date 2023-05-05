package com.zhulang.basic.utils.common.exception;

import lombok.Getter;
/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
@Getter
public enum CommonErrorCode implements IErrorCode {

    SYSTEM_ERROR(9999,"系统开小差"),
    RPC_FAIL(9998, "外部接口访问异常,method=%s"),
    PARAM_INVALID(9997,"参数不合法"),
    INVOKE_SERVER_FAIL(9996, "访问%s服务异常"),
    ;


    @Override
    public Integer getErrorCode() {

        return getCode();
    }

    @Override
    public String getErrorMsg() {

        return getMsg();
    }


    private Integer code;
    private String msg;

    CommonErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
