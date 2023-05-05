package com.zhulang.basic.utils.common.exception;

import com.zhulang.basic.utils.common.constants.Constants;
import com.zhulang.basic.utils.common.response.IResponseCode;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class SysException extends BaseException {
    private static final long serialVersionUID = 2646977714693654216L;

    public SysException(String errMessage) {
        super(Constants.DEFAULT_ERR_CODE, errMessage);
    }

    public SysException(IResponseCode responseCode) {
        super(responseCode.getResultCode(), responseCode.getResultMsg());
    }

    public SysException(int errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public SysException(String errMessage, Throwable e) {
        super(Constants.DEFAULT_ERR_CODE, errMessage, e);
    }

    public SysException(int errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}