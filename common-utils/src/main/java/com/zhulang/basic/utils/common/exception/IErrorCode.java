package com.zhulang.basic.utils.common.exception;

/**
 * declare your system ErrorCode like the ErrorCode class in test package
 * @Author zhulang
 * @Date 2023-05-05
 **/
public interface IErrorCode {

    /**
     * get error code
     *
     * @return
     */
    Integer getErrorCode();


    /**
     * get error message
     *
     * @return
     */
    String getErrorMsg();
}
