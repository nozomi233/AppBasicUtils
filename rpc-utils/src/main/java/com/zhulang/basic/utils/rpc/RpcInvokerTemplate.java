package com.zhulang.basic.utils.rpc;

import com.github.rholder.retry.*;
import com.google.common.base.Splitter;
import com.zhulang.basic.utils.common.exception.CommonErrorCode;
import com.zhulang.basic.utils.common.exception.DependencyException;
import com.zhulang.basic.utils.common.exception.IErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
@Slf4j
public class RpcInvokerTemplate {


    /**
     * 基本调用 远程rpc接口响应不是标准Result
     *
     * @param callable
     * @param serviceName
     * @param param
     * @param <T>
     * @return
     */
    public static <T> T invoke(Callable<T> callable, String serviceName, Object param) {

        T result = null;
        long start = System.currentTimeMillis();
        try {
            result = callable.call();
            if (result == null) {
                throw new DependencyException(CommonErrorCode.RPC_FAIL,serviceName);
            }
            return result;
        } catch (Exception e) {
            log.error("invoke remote service = {} exception, param = {}", serviceName, JacksonUtils.toJsonString(param), e);
            throw new DependencyException(CommonErrorCode.RPC_FAIL,serviceName);
        } finally {
            long end = System.currentTimeMillis();
            String resultStr = JacksonUtils.toJsonString(result);
            if (resultStr.length() < 1024 * 3) {
                log.info("invoke remote service {},cost={}ms,param = {},result = {}", serviceName, end - start, JacksonUtils.toJsonString(param), resultStr);
            } else {
                log.info("invoke remote service {},cost={}ms,param = {}", serviceName, end - start, JacksonUtils.toJsonString(param));
                List<String> strs = Splitter.fixedLength(1024 * 3).splitToList(resultStr);
                for (int i = 0; i < strs.size() && i < 3; i++) {
                    log.info("service {},resultIndex={},result={}", serviceName, i, strs.get(i));
                }
            }
        }
    }

    /**
     * 基本调用 响应是标准Result
     *
     * @param callable
     * @param serviceName
     * @param param
     * @param <T>
     * @return
     */
    public static <T> T commonInvoke(Callable<Result<T>> callable, String serviceName, Object param) {

        Result<T> result = null;
        long start = System.currentTimeMillis();
        try {
            result = callable.call();
            if (result == null || BooleanUtils.isFalse(result.getSuccess())) {
                throw new DependencyException(CommonErrorCode.RPC_FAIL,serviceName);
            }
            return result.getData();
        } catch (Exception e) {
            log.error("invoke remote service = {} exception, param = {}", serviceName, JacksonUtils.toJsonString(param), e);
            throw new DependencyException(CommonErrorCode.RPC_FAIL,serviceName);
        } finally {
            long end = System.currentTimeMillis();
            String resultStr = JacksonUtils.toJsonString(result);
            if (resultStr.length() < 1024 * 3) {
                log.info("invoke remote service {},cost={}ms,param = {},result = {}", serviceName, end - start, JacksonUtils.toJsonString(param), resultStr);
            } else {
                log.info("invoke remote service {},cost={}ms,param = {}", serviceName, end - start, JacksonUtils.toJsonString(param));
                List<String> strs = Splitter.fixedLength(1024 * 3).splitToList(resultStr);
                for (int i = 0; i < strs.size() && i < 3; i++) {
                    log.info("service {},resultIndex={},result={}", serviceName, i, strs.get(i));
                }
            }
        }
    }

    /**
     * 基本调用 远程rpc接口响应不是标准Result
     *
     * @param callable
     * @param serviceName
     * @param param
     * @param <T>
     * @return
     */
    public static <T> T invoke(Callable<T> callable, String serviceName, Object param, IErrorCode errorCode) {

        T result = null;
        long start = System.currentTimeMillis();
        try {
            result = callable.call();
            if (result == null) {
                throw new DependencyException(errorCode);
            }
            return result;
        } catch (Exception e) {
            log.error("invoke remote service = {} exception, param = {}", serviceName, JacksonUtils.toJsonString(param), e);
            throw new DependencyException(errorCode);
        } finally {
            long end = System.currentTimeMillis();
            String resultStr = JacksonUtils.toJsonString(result);
            if (resultStr.length() < 1024 * 3) {
                log.info("invoke remote service {},cost={}ms,param = {},result = {}", serviceName, end - start, JacksonUtils.toJsonString(param), resultStr);
            } else {
                log.info("invoke remote service {},cost={}ms,param = {}", serviceName, end - start, JacksonUtils.toJsonString(param));
                List<String> strs = Splitter.fixedLength(1024 * 3).splitToList(resultStr);
                for (int i = 0; i < strs.size() && i < 3; i++) {
                    log.info("service {},resultIndex={},result={}", serviceName, i, strs.get(i));
                }
            }
        }
    }

    /**
     * 基本调用
     *
     * @param callable
     * @param serviceName
     * @param param
     * @param <T>
     * @return
     */
    public static <T> T commonInvoke(Callable<Result<T>> callable, String serviceName, Object param, IErrorCode errorCode) {

        Result<T> result = null;
        long start = System.currentTimeMillis();
        try {
            result = callable.call();
            if (result == null) {
                throw new DependencyException(errorCode);
            }
            return result.getData();
        } catch (Exception e) {
            log.error("invoke remote service = {} exception, param = {}", serviceName, JacksonUtils.toJsonString(param), e);
            throw new DependencyException(errorCode);
        } finally {
            long end = System.currentTimeMillis();
            String resultStr = JacksonUtils.toJsonString(result);
            if (resultStr.length() < 1024 * 3) {
                log.info("invoke remote service {},cost={}ms,param = {},result = {}", serviceName, end - start, JacksonUtils.toJsonString(param), resultStr);
            } else {
                log.info("invoke remote service {},cost={}ms,param = {}", serviceName, end - start, JacksonUtils.toJsonString(param));
                List<String> strs = Splitter.fixedLength(1024 * 3).splitToList(resultStr);
                for (int i = 0; i < strs.size() && i < 3; i++) {
                    log.info("service {},resultIndex={},result={}", serviceName, i, strs.get(i));
                }
            }
        }
    }

    /**
     * 基本调用 远程rpc接口响应不是标准Result
     *
     * @param callable
     * @param serviceName
     * @param param
     * @param errorCode
     * @param retryPredicate true：重试
     * @param <T>
     * @return
     */
    public static <T> T retryInvoke(Callable<T> callable, String serviceName, Object param, IErrorCode errorCode, Predicate<T> retryPredicate) {
        try {
            RetryerBuilder<T> retryBuilder = RetryerBuilder.<T>newBuilder();
            if (Objects.nonNull(retryPredicate)) {
                retryBuilder.retryIfResult(retryPredicate::test);
            }
            Retryer<T> retry = retryBuilder
                    .retryIfException()
                    .withRetryListener(new RpcInvokerRetryListener())
                    .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                    .build();
            return invoke(() -> retry.call(callable), serviceName, param, errorCode);
        } catch (Exception e) {
            throw new DependencyException(errorCode);
        }
    }


    /**
     * 基本调用 远程rpc接口响应不是标准Result
     *
     * @param callable
     * @param serviceName
     * @param param
     * @param retryPredicate true：重试
     * @param <T>
     * @return
     */
    public static <T> T retryInvoke(Callable<T> callable, String serviceName, Object param, Predicate<T> retryPredicate) {
        return retryInvoke(callable, serviceName, param, CommonErrorCode.RPC_FAIL, retryPredicate);
    }

    public static <T> T retryInvoke(Callable<T> callable, String serviceName, Object param) {
        return retryInvoke(callable, serviceName, param, CommonErrorCode.RPC_FAIL, null);
    }


    @Slf4j
    private static class RpcInvokerRetryListener implements RetryListener {

        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            if (attempt.getAttemptNumber() > 1) {
                log.warn("retry time: {} cause :{} ", attempt.getAttemptNumber() - 1, attempt.hasException() ? attempt.getExceptionCause().toString() : JacksonUtils.toJsonString(attempt.getResult()));
            }

        }
    }
}
