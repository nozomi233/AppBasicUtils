package com.zhulang.common.common.alert;

import lombok.Data;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Data
public class MetricsModel {
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 类名
     */
    private String iface;

    /**
     * 成功标识
     */
    private boolean success;

    /**
     * 响应时间
     */
    private Long rt;

    /**
     * 异常类
     */
    private Exception exception;
}
