package com.zhulang.basic.utils.common.exception;

import com.zhulang.basic.utils.common.constants.Constants;
import com.zhulang.basic.utils.common.response.IResponseCode;

/**
 * 流程异常，不会报警
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class ProcessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ProcessException(String errMessage) {
        super(Constants.DEFAULT_PROCESS_ERR_CODE, errMessage);
    }

    public ProcessException(IResponseCode responseCode) {
        super(responseCode.getResultCode(), responseCode.getResultMsg());
    }

    public ProcessException(int errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public ProcessException(String errMessage, Throwable e) {
        super(Constants.DEFAULT_PROCESS_ERR_CODE, errMessage, e);
    }

    public ProcessException(int errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}