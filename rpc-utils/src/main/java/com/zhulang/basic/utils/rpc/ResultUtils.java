package com.zhulang.basic.utils.rpc;


import com.zhulang.basic.utils.common.exception.BaseException;
import com.zhulang.basic.utils.common.exception.CommonErrorCode;
import com.zhulang.basic.utils.common.exception.IErrorCode;

import java.util.Objects;
/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class ResultUtils {

    private static final Integer SUCCESS_CODE = 0;

    public static <T> Result<T> successResult(T data) {

        Result<T> result = new Result<>();
        result.setData(data);
        result.setSuccess(true);
        result.setCode(SUCCESS_CODE);

        return result;
    }

    public static <T> Result<T> failResult(Integer code, String message) {

        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(message);

        return result;
    }

    public static <T> Result<T> failResult(IErrorCode errorCode) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        if(Objects.isNull(errorCode)){
            errorCode = CommonErrorCode.SYSTEM_ERROR;
        }
        result.setCode(errorCode.getErrorCode());
        result.setMsg(errorCode.getErrorMsg());

        return result;
    }

    public static <T> Result<T> failResult(BaseException baseException) {

        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(baseException.getErrCode());
        result.setMsg(baseException.getMessage());

        return result;
    }


}
