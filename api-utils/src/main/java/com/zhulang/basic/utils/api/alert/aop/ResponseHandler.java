package com.zhulang.basic.utils.api.alert.aop;

import com.zhulang.basic.utils.common.response.Response;
import com.zhulang.basic.utils.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * 相应处理
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Slf4j
public class ResponseHandler {

    public static Object handle(Class returnType, int errCode, String errMsg) {
        if (isResponse(returnType)) {
            return handleResponse(returnType, errCode, errMsg);
        }
//        if (isCarKeyProto(returnType)) {
//            return handleCarKeyProto(returnType, errCode, errMsg);
//        }
//        if (isHbProto(returnType)) {
//            return handleHbProto(returnType, errCode, errMsg);
//        }
        log.error("unknown returnType");
        return null;
    }

    public static Object handle(Class returnType, BaseException e) {
        return handle(returnType, e.getErrCode(), e.getMessage());
    }

    private static Object handleResponse(Class returnType, int errCode, String errMsg) {
        try {
            Response response = (Response) returnType.newInstance();
            response.setCode(errCode);
            response.setMsg(errMsg);
            return response;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

//    private static Object handleCarKeyProto(Class returnType, int errCode, String errMsg) {
//        try {
//            Proto proto = (Proto) returnType.newInstance();
//            proto.setCode(errCode);
//            proto.setMsg(errMsg);
//            return proto;
//        } catch (Exception ex) {
//            LogUtils.ERROR.error(ex.getMessage(), ex);
//            return null;
//        }
//    }

//    private static Object handleHbProto(Class returnType, int errCode, String errMsg) {
//        try {
//            com.hellobike.base.model.Proto proto = (com.hellobike.base.model.Proto) returnType.newInstance();
//            proto.setCode(errCode);
//            proto.setMsg(errMsg);
//            return proto;
//        } catch (Exception ex) {
//            LogUtils.ERROR.error(ex.getMessage(), ex);
//            return null;
//        }
//    }

    private static boolean isResponse(Class returnType) {
        return returnType == Response.class || returnType.getGenericSuperclass() == Response.class;
    }

//    private static boolean isCarKeyProto(Class returnType) {
//        return returnType == Proto.class || returnType.getGenericSuperclass() == Proto.class;
//    }
//
//    private static boolean isHbProto(Class returnType) {
//        return returnType == com.hellobike.base.model.Proto.class || returnType.getGenericSuperclass() == com.hellobike.base.model.Proto.class;
//    }
}
