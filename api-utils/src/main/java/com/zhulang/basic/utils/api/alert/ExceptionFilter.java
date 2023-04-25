package com.zhulang.common.common.alert;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public interface ExceptionFilter {

    /**
     * 是否过滤异常信息
     *
     * @param exception
     * @return
     */
    boolean filterException(Exception exception);

    /**
     * 消息异常过滤
     *
     * @param exception
     * @return
     */
    boolean filterMsgException(Exception exception);


    /**
     * 慢方法过滤
     *
     * @return
     */
    boolean filterSlowMethod(String methodName);


    boolean filterMessage(String message);
}
