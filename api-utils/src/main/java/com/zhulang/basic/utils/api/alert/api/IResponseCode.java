package com.zhulang.common.common.alert.api;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public interface IResponseCode {
    /**
     * 结果码
     * @return
     */
    Integer getResultCode();

    /**
     * 错误码信息
     * @return
     */
    String getResultMsg();
}
