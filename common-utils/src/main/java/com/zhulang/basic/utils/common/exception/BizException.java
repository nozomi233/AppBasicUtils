package com.zhulang.basic.utils.common.exception;

import com.zhulang.basic.utils.common.constants.Constants;
import com.zhulang.basic.utils.common.response.IResponseCode;

/**
 * 业务异常
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(String errMessage) {
        super(Constants.DEFAULT_BIZ_ERR_CODE, errMessage);
    }

    public BizException(IResponseCode responseCode) {
        super(responseCode.getResultCode(), responseCode.getResultMsg());
    }

    public BizException(int errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public BizException(String errMessage, Throwable e) {
        super(Constants.DEFAULT_BIZ_ERR_CODE, errMessage, e);
    }

    public BizException(int errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}