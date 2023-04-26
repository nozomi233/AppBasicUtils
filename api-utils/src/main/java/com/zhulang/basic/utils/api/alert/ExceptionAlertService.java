package com.zhulang.basic.utils.api.alert;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.zhulang.basic.utils.api.exception.ProcessException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zhulang.basic.utils.api.consts.Constants.*;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/

@Slf4j
@Service
@Configurable
public class ExceptionAlertService {

    public static String dingDingToken;

    @Value("${alert.dingDingToken:8ffa0a3aefff0604eb29ca32a3de00bb3ab2a9dd9c31a66970ad10f517374781}")
    public void setDingDingToken(String dingDingToken) {
        ExceptionAlertService.dingDingToken = dingDingToken;
    }

    private static String bizDingDingToken;

    @Value("${alert.bizDingDingToken:8ffa0a3aefff0604eb29ca32a3de00bb3ab2a9dd9c31a66970ad10f517374781}")
    public void setBizDingDingToken(String bizDingDingToken) {
        ExceptionAlertService.bizDingDingToken = bizDingDingToken;
    }

    private static Boolean exceptionReport;

    @Value("${alert.exceptionReport:true}")
    public void setExceptionReport(Boolean exceptionReport) {
        ExceptionAlertService.exceptionReport = exceptionReport;
    }

    private static long slowMethodTime = 3000;

    @Value("${common.slowMethodTime:3000}")
    public void setSlowMethodTime(long slowMethodTime) {
        ExceptionAlertService.slowMethodTime = slowMethodTime;
    }


    private static String exceptionAtManager = "[]";

    @Value("${common.exceptionAtManager:[]}")
    public void setExceptionAtManager(String exceptionAtManager) {
        ExceptionAlertService.exceptionAtManager = exceptionAtManager;
    }

    private List<ExceptionAtManager> parseExceptionAtManager() {
        try {
            return JSON.parseArray(exceptionAtManager, ExceptionAtManager.class);
        } catch (Exception e) {
            // todo zl
//            LogUtils.ERROR.error("parseExceptionAtManager error,exceptionAtManager={}", exceptionAtManager, e);
            return new ArrayList<>();
        }
    }

    @Data
    @EqualsAndHashCode
    static class ExceptionAtManager {
        private String pattern;
        private List<String> phones;
        private String desc;
    }

    private static final DingTalkService dingTalkService = new DingTalkService();

    private static ExceptionFilter exceptionFilter = new DefaultExceptionFilter();

    public void report(MetricsModel metricsModel) {

        if (metricsModel == null) {
            return;
        }

        // todo zl
//        String reqId = Etrack.getReqId();
        String reqId = "mock";
        String title = MDC.get(MDC_TITLE_ID);
        String traceId = MDC.get(TRACE_ID);

        if (metricsModel.getRt() != null && metricsModel.getRt() > slowMethodTime) {
            // 慢方法上报
            slowMethodReport(metricsModel, reqId, title, traceId);
        }

        if (metricsModel.getException() != null) {
            // 异常上报
            exportReport(metricsModel, reqId, title, traceId);
        }
    }

    private void slowMethodReport(MetricsModel metricsModel, String reqId, String title, String traceId) {
        THREAD_POOL.execute(() -> {
            try {
                MDC.put(MDC_TITLE_ID, title);
                MDC.put(REQ_ID, reqId);
                MDC.put(TRACE_ID, traceId);
                StringBuilder sb = new StringBuilder();
                sb.append("【").append(Environment.ENV).append("--慢方法告警】").append("\n");
                sb.append("【应用】").append(Environment.APP_ID).append("\n");
                sb.append("【接口】").append(metricsModel.getIface()).append("#").append(metricsModel.getMethodName()).append("\n");
                sb.append("【耗时】").append(metricsModel.getRt()).append("ms").append("\n");
                String hlogUrl = null;
                String setUrl = null;
                long now = System.currentTimeMillis();
                if (StringUtils.isNotBlank(traceId)) {
                    if ("pro".equals(Environment.ENV)) {
                        setUrl = String.format(SET_PRO, traceId, now);
                        hlogUrl = String.format(PRO_HLOG, Environment.APP_ID, now - 30000, now + 30000, traceId);
                    } else if ("fat".equals(Environment.ENV)) {
                        setUrl = String.format(SET_TEST, Environment.ENV, traceId, now);
                        hlogUrl = String.format(FAT_HLOG, Environment.APP_ID, now - 30000, now + 30000, traceId);
                    } else if ("uat".equals(Environment.ENV)) {
                        setUrl = String.format(SET_TEST, Environment.ENV, traceId, now);
                        hlogUrl = String.format(UAT_HLOG, Environment.APP_ID, now - 30000, now + 30000, traceId);
                    } else if ("pre".equals(Environment.ENV)) {
                        setUrl = String.format(SET_TEST, Environment.ENV, traceId, now);
                        hlogUrl = String.format(PRE_HLOG, Environment.APP_ID, now - 30000, now + 30000, traceId);
                    }
                    sb.append("[set]").append("(").append(setUrl).append(")").append("&ensp;&ensp;");
                    sb.append("[hlog]").append("(").append(hlogUrl).append(")");
                }
                String text = sb.toString().replace("\n", "  \n");
                if (exceptionFilter.filterSlowMethod(metricsModel.getMethodName())) {
                    //todo 业务方区别
                    dingTalkService.send(text, "业务告警", bizDingDingToken, null);
                } else {
                    dingTalkService.send(text, "业务告警", dingDingToken, null);
                }
            } catch (Exception e) {
                log.warn("slowCostTime error", e);
            } finally {
                MDC.clear();
            }
        });
    }

    private void exportReport(MetricsModel metricsModel, String reqId, String innerId, String traceId) {
        THREAD_POOL.execute(() -> {
            try {
                MDC.put(MDC_TITLE_ID, innerId);
                MDC.put(REQ_ID, reqId);
                MDC.put(TRACE_ID, traceId);
                if (metricsModel.getException() instanceof ProcessException) {
                    return;
                }
                doReport(metricsModel.getException(), metricsModel.getIface(), metricsModel.getMethodName());
            } catch (Exception e) {
                log.warn("ExceptionReportService doReport error", e);
            } finally {
                MDC.clear();
            }
        });
    }


    private void doReport(Exception exception, String iface, String methodName) {
        ExceptionReportVo exceptionReportVo = new ExceptionReportVo(exception);

        StringBuilder sb = new StringBuilder();
        String stackTrace = exceptionReportVo.getStackTrace();
        sb.append("【").append(Environment.ENV).append("--异常告警】").append("\n");
        sb.append("【应用】").append(Environment.APP_ID).append("\n");
        if (StringUtils.isNotBlank(methodName)) {
            sb.append("【接口】").append(iface).append("#").append(methodName).append("\n");
        } else {
            sb.append("\n");
        }
        sb.append("【报错信息】\n");
        sb.append(stackTrace).append("\n");
        String kibanaUrl = null;
        String setUrl = null;
        String hlogUrl = null;
        long now = System.currentTimeMillis();
        // todo zl 这里mock下traceId
        exceptionReportVo.setTraceId("test20230424");
        if (StringUtils.isNotBlank(exceptionReportVo.getTraceId())) {
            if ("pro".equals(Environment.ENV)) {
                kibanaUrl = String.format(ES_PRO, exceptionReportVo.getReqid());
                setUrl = String.format(SET_PRO, exceptionReportVo.getTraceId(), now);
                hlogUrl = String.format(PRO_HLOG, Environment.APP_ID, now - 30000, now + 30000, exceptionReportVo.getTraceId());
            } else if ("fat".equals(Environment.ENV)) {
                setUrl = String.format(SET_TEST, Environment.ENV, exceptionReportVo.getTraceId(), now);
                kibanaUrl = String.format(ES_FAT, exceptionReportVo.getReqid());
                hlogUrl = String.format(FAT_HLOG, Environment.APP_ID, now - 30000, now + 30000, exceptionReportVo.getTraceId());
            } else if ("uat".equals(Environment.ENV)) {
                setUrl = String.format(SET_TEST, Environment.ENV, exceptionReportVo.getTraceId(), now);
                kibanaUrl = String.format(ES_UAT, exceptionReportVo.getReqid());
                hlogUrl = String.format(UAT_HLOG, Environment.APP_ID, now - 30000, now + 30000, exceptionReportVo.getTraceId());
            } else if ("pre".equals(Environment.ENV)) {
                setUrl = String.format(SET_TEST, Environment.ENV, exceptionReportVo.getTraceId(), now);
                kibanaUrl = String.format(ES_PRE, exceptionReportVo.getReqid());
                hlogUrl = String.format(PRE_HLOG, Environment.APP_ID, now - 30000, now + 30000, exceptionReportVo.getTraceId());
            } else if ("test".equals(Environment.ENV)) {
                setUrl = String.format(SET_TEST, Environment.ENV, exceptionReportVo.getTraceId(), now);
                kibanaUrl = String.format(ES_FAT, exceptionReportVo.getReqid());
                hlogUrl = String.format(FAT_HLOG, Environment.APP_ID, now - 30000, now + 30000, exceptionReportVo.getTraceId());
            }
            sb.append("[es-kibana]").append("(").append(kibanaUrl).append(")").append("&ensp;&ensp;");
            sb.append("[set]").append("(").append(setUrl).append(")").append("&ensp;&ensp;");
            sb.append("[hlog]").append("(").append(hlogUrl).append(")");
        }

        if (exceptionFilter.filterException(exception)) {
            if (BooleanUtils.isTrue(exceptionReport)) {
                String text = sb.toString().replace("\n", "  \n");
                dingTalkService.send(text, "业务报警", bizDingDingToken, null);
            }
        } else {
            OapiRobotSendRequest.At at = null;
            List<ExceptionAtManager> exceptionAtManagers = parseExceptionAtManager();
            if (exceptionAtManagers != null && exceptionAtManagers.size() > 0 && exception != null) {
                String message = exception.getMessage();
                if (message != null) {
                    for (ExceptionAtManager atManager : exceptionAtManagers) {
                        String pattern = atManager.getPattern();
                        if (message.equals(pattern) || (pattern.contains("*") && PatternMatchUtils.simpleMatch(pattern, message))) {
                            List<String> phones = atManager.getPhones();
                            if (phones != null && phones.size() > 0) {
                                at = new OapiRobotSendRequest.At();
                                at.setAtMobiles(phones);
                                sb.append("\n");
                                for (String phone : phones) {
                                    sb.append("@").append(phone).append("&ensp;&ensp;");
                                }
                                if (atManager.getDesc() != null) {
                                    sb.append("&ensp;&ensp;");
                                    sb.append(atManager.getDesc());
                                }
                                break;
                            }
                        }
                    }
                }
            }
            String text = sb.toString().replace("\n", "  \n");
            dingTalkService.send(text, "业务报警", dingDingToken, at);
        }
    }
}
