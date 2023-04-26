package com.zhulang.basic.utils.api.alert.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhulang.basic.utils.api.alert.DefaultExceptionFilter;
import com.zhulang.basic.utils.api.alert.ExceptionAlertService;
import com.zhulang.basic.utils.api.alert.ExceptionFilter;
import com.zhulang.basic.utils.api.alert.MetricsModel;
import com.zhulang.basic.utils.api.alert.aop.annotation.ApiRunTimeLog;
import com.zhulang.basic.utils.api.consts.Constants;
import com.zhulang.basic.utils.api.enums.ResponseCodeEnum;
import com.zhulang.basic.utils.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Aspect
@Slf4j
@Component
public class ApiRunTimeLogAspect {

    @Value("${alert.needAlert:true}")
    private Boolean needAlert;

    @Value("${alert.needValidateParam:true}")
    private Boolean needValidateParam;

    @Value("${alert.needHandleResp:true}")
    private Boolean needHandleResp;

    @Resource
    private ExceptionAlertService exceptionAlertService;

    private ExceptionFilter exceptionFilter = new DefaultExceptionFilter();

    public static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Pointcut("@within(com.zhulang.basic.utils.api.alert.aop.annotation.ApiRunTimeLog) && execution(public * *(..))")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logRequest(joinPoint);

        Object response = null;
        Exception exception = null;
        Method method = getMethod(joinPoint);
        String methodName = method.getName();
        String iface = getIface(joinPoint);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean createTitle = false;
        try {
            String title = MDC.get(Constants.MDC_TITLE_ID);
            if (title == null) {
                createTitle = true;
                MDC.put(Constants.MDC_TITLE_ID, Optional.ofNullable(MDC.get(Constants.SPAN_ID)).orElse(RandomStringUtils.randomAlphanumeric(10)));
            }
            //入参校验
            if (needValidateParam) {
                validateParam(joinPoint);
            }
            response = joinPoint.proceed();
        } catch (Throwable e) {
            exception = (Exception) e;
            if(!needHandleResp){
                throw e;
            }
            response = handleException(joinPoint, e);
        } finally {
            logResponse(startTime, response);
            if (needAlert) {
                alertException(joinPoint, exception, method, methodName, iface, stopWatch, createTitle);
            }
        }
        return response;
    }

    /**
     * 异常告警处理
     */
    private void alertException(ProceedingJoinPoint joinPoint, Exception exception, Method method, String methodName, String iface, StopWatch stopWatch, boolean createTitle) {
        try {
            stopWatch.stop();
            long costTime = stopWatch.getTime();
            ApiRunTimeLog annotation = getAnnotation(joinPoint);
            safetyReportMetrics(methodName, iface, exception, costTime, annotation.alertReport());
            // todo zl reqId
//            String reqId = Etrack.getReqId();
            String reqId = "1111";
            if (StringUtils.isNotBlank(reqId)) {
                log.info("reqId={},method={}", reqId, method);
            }
        } catch (Exception e) {
            log.warn("report exception ", e);
        } finally {
            if (createTitle) {
                MDC.remove(Constants.MDC_TITLE_ID);
            }
        }
    }

    /**
     * 校验入参
     *
     * @param joinPoint
     */
    private void validateParam(ProceedingJoinPoint joinPoint) {
        for (Object param : joinPoint.getArgs()) {
            if (Objects.isNull(param)) {
                log.error("param is null");
                ExceptionFactory.bizException(ResponseCodeEnum.ILLEGAL_PARAM);
            }
            Set<ConstraintViolation<Object>> validators = validator.validate(param);
            for (ConstraintViolation<Object> item : validators) {
                log.error("validate param error {}", item.getMessage());
                ExceptionFactory.bizException(item.getMessage());
            }
        }
    }

    /**
     * 处理异常
     *
     * @param joinPoint
     * @param e
     * @return
     */
    private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String originRequestStr = JSONObject.toJSONString(joinPoint.getArgs());
        Class returnType = ms.getReturnType();

        if(knownException(e)){
            return ResponseHandler.handle(returnType, (BaseException) e);
        }
        log.error("UNKNOWN EXCEPTION ，param : {} , errorMsg: {}", originRequestStr, e.getMessage(), e);
        return ResponseHandler.handle(returnType, ResponseCodeEnum.SYSTEM_ERROR.getCode(), ResponseCodeEnum.SYSTEM_ERROR.getMsg());
    }


    private void logResponse(long startTime, Object response) {
        try {
            long endTime = System.currentTimeMillis();
            log.debug("RESPONSE : " + JSON.toJSONString(response));
            log.debug("COST : " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            //swallow it
            log.error("logResponse error : " + e);
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        try {
            if (!log.isDebugEnabled()) {
                return;
            }
            log.debug("START PROCESSING: " + joinPoint.getSignature().toShortString());
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                log.debug("REQUEST : " + JSON.toJSONString(arg, SerializerFeature.IgnoreErrorGetter));
            }
        } catch (Exception e) {
            //swallow it
            log.error("logRequest error : " + e);
        }
    }

    private void safetyReportMetrics(String method, String iface, Exception exception, long costTime, Boolean alertReport) {
        MetricsModel metricsData = getMetricsData(method, iface, exception, costTime);
        doReportMetrics(metricsData, alertReport);
    }


    /**
     * 异常数据上报
     *
     * @param metricsData
     */
    private void doReportMetrics(MetricsModel metricsData, boolean alertReport) {
        if (exceptionAlertService != null && alertReport) {
            exceptionAlertService.report(metricsData);
        }
    }

    private MetricsModel getMetricsData(String method, String iface, Exception exception, long costTime) {

        MetricsModel metricsModel = new MetricsModel();
        metricsModel.setIface(iface);
        metricsModel.setMethodName(method);
        metricsModel.setException(exception);
        //todo 响应异常
        metricsModel.setSuccess(true);
        metricsModel.setRt(costTime);

        return metricsModel;
    }

    /**
     * 拿到方法名
     *
     * @param joinPoint
     * @return
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature ms = (MethodSignature) signature;
        return ms.getMethod();
    }

    private String getIface(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    private ApiRunTimeLog getAnnotation(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getAnnotation(ApiRunTimeLog.class);
    }

    private Boolean knownException(Throwable e) {
        if (e instanceof BizException || e instanceof ProcessException) {
            log.warn("BIZ EXCEPTION : " + e.getMessage(), e);
            //在Debug的时候，对于BizException也打印堆栈
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return true;
        }
        if (e instanceof DbException) {
            log.error("DB EXCEPTION :" + e.getMessage(), e);
            return true;
        }
        if (e instanceof RpcException) {
            log.error("RPC EXCEPTION :" + e.getMessage(), e);
            return true;
        }
        if (e instanceof SysException) {
            log.error("SYS EXCEPTION :" + e.getMessage(), e);
            return true;
        }
        return false;
    }
}
