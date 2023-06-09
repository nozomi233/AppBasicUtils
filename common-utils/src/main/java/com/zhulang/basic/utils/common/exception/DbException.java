package com.zhulang.basic.utils.common.exception;

import com.zhulang.basic.utils.common.constants.Constants;
import com.zhulang.basic.utils.common.response.IResponseCode;

/**
 * 数据库异常
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class DbException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DbException(String errMessage) {
        super(Constants.DEFAULT_DB_ERR_CODE, errMessage);
    }

    public DbException(IResponseCode responseCode) {
        super(responseCode.getResultCode(), responseCode.getResultMsg());
    }

    public DbException(int errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public DbException(String errMessage, Throwable e) {
        super(Constants.DEFAULT_DB_ERR_CODE, errMessage, e);
    }

    public DbException(int errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}