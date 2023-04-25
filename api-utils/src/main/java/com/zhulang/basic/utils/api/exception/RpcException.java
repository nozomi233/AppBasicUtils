package com.zhulang.common.common.exception;

import com.zhulang.common.common.alert.api.IResponseCode;
import com.zhulang.common.common.consts.Constants;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class RpcException extends BaseException {

    private static final long serialVersionUID = 1L;

    public RpcException(String errMessage) {
        super(Constants.DEFAULT_RPC_ERR_CODE, errMessage);
    }

    public RpcException(IResponseCode responseCode) {
        super(responseCode.getResultCode(), responseCode.getResultMsg());
    }

    public RpcException(int errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public RpcException(String errMessage, Throwable e) {
        super(Constants.DEFAULT_RPC_ERR_CODE, errMessage, e);
    }

    public RpcException(int errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}