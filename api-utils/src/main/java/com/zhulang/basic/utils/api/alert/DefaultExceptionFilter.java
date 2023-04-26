package com.zhulang.basic.utils.api.alert;


import com.alibaba.fastjson.JSON;
import com.zhulang.basic.utils.api.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Slf4j
public class DefaultExceptionFilter implements ExceptionFilter {
    private static String commonFilterError = "[]";

    private static String dependencyFilterError = "[]";

    private static String msgFilterError = "[]";

    private static String slowMethodNames = "[]";

    @Value("${common.filterError:[]}")
    public void setCommonFilterError(String commonFilterError) {
        DefaultExceptionFilter.commonFilterError = commonFilterError;
    }

    @Value("${dependency.filterError:[]}")
    public void setDependencyFilterError(String dependencyFilterError) {
        DefaultExceptionFilter.dependencyFilterError = dependencyFilterError;
    }

    @Value("${msg.filterError:[]}")
    public void setMsgFilterError(String msgFilterError) {
        DefaultExceptionFilter.msgFilterError = msgFilterError;
    }

    @Value("${filter.slowMethodNames:[]}")
    public void setSlowMethodNames(String slowMethodNames) {
        DefaultExceptionFilter.slowMethodNames = slowMethodNames;
    }

    @Override
    public boolean filterException(Exception exception) {
        return doFilter(exception, getFilters(commonFilterError));
    }

    @Override
    public boolean filterMsgException(Exception exception) {
        return doFilter(exception, getFilters(msgFilterError));
    }

    @Override
    public boolean filterSlowMethod(String methodName) {
        List<String> filterSlowMethodNames = getFilters(slowMethodNames);
        if (filterSlowMethodNames != null && filterSlowMethodNames.size() > 0) {
            return filterSlowMethodNames.contains(methodName);
        }
        return false;
    }

    @Override
    public boolean filterMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return true;
        }

        List<String> filters = getFilters(commonFilterError);
        if (CollectionUtils.isEmpty(filters)) {
            return false;
        }

        // 异常信息过滤
        if (filters.contains(message)) {
            return true;
        }

        for (String pattern : filters) {
            if (pattern.contains("*") && PatternMatchUtils.simpleMatch(pattern, message)) {
                return true;
            }
        }

        return false;
    }

    private boolean doFilter(Exception exception, List<String> filterErrorList) {
        if (exception == null) {
            return true;
        }

        if (filterErrorList == null || filterErrorList.isEmpty()) {
            return false;
        }

        String error = "";
        if (exception instanceof BaseException) {
            BaseException be = (BaseException) exception;
            error = String.valueOf(be.getErrCode());
        }

        // 异常码过滤
        if (filterErrorList.contains(error)) {
            return true;
        }

        if (exception.getMessage() != null) {

            // 异常信息过滤
            if (filterErrorList.contains(exception.getMessage())) {
                return true;
            }

            for (String pattern : filterErrorList) {
                if (pattern.contains("*") && PatternMatchUtils.simpleMatch(pattern, exception.getMessage())) {
                    return true;
                }
            }

        }
        return false;
    }

    private List<String> getFilters(String filterNames) {
        List<String> filters;
        try {
            filters = JSON.parseArray(filterNames, String.class);
        } catch (Exception e) {
            log.error("parse filterError error", e);
            filters = new ArrayList<>();
        }
        return filters;
    }
}
