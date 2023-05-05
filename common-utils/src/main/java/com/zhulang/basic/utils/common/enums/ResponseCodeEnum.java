package com.zhulang.basic.utils.common.enums;

import com.zhulang.basic.utils.common.response.IResponseCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author zhulang
 * @Date 2023-04-25
 **/
public enum ResponseCodeEnum implements IResponseCode {
    /**
     * 响应结果码
     */
    SOA_SUCCESS(0, "请求成功"),
    RPC_SUCCESS(200, "请求成功"),
    ILLEGAL_PARAM(300, "非法入参"),
    SYSTEM_ERROR(500, "系统异常"),
    ;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    ResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResponseCodeEnum parseFromMsg(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        for (ResponseCodeEnum currentEnum : ResponseCodeEnum.values()) {
            if (currentEnum.msg.equals(msg)) {
                return currentEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Integer getResultCode() {
        return getCode();
    }

    @Override
    public String getResultMsg() {
        return getMsg();
    }
}
