package com.zhulang.basic.utils.api.alert;

import com.zhulang.basic.utils.common.StackTraceUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import static com.zhulang.basic.utils.common.constants.Constants.*;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Data
public class ExceptionReportVo {
    private transient Exception exception;

    private String reqid;

    private String traceId;

    private String innerId;

    private String stackTrace;

    private String shortMsg;

    public ExceptionReportVo(Exception exception) {
        this.exception = exception;
        this.reqid = MDC.get(REQ_ID);
        this.traceId = MDC.get(TRACE_ID);
        this.innerId = MDC.get(MDC_TITLE_ID);
        this.stackTrace = StackTraceUtils.getStackTrace(exception);
        this.shortMsg = StringUtils.defaultString(exception.getMessage(), "").replace('"', '\'');
    }
}
