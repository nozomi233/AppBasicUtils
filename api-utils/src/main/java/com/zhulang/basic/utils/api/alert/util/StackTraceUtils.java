package com.zhulang.common.common.alert.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class StackTraceUtils {
    public static String getStackTrace(Exception exception) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        if (StringUtils.isNotEmpty(stackTrace)) {
            int indexOf = stackTrace.indexOf("\n\t");
            int index = 0;
            for (int i = 0; i < 6 && indexOf > 0; i++) {
                index = indexOf;
                indexOf = stackTrace.indexOf("\n\t", indexOf + 2);
            }
            if (index > 0) {
                stackTrace = stackTrace.substring(0, index);
            }

            stackTrace = stackTrace.replace("at\n\t", "");
            String[] stackTraceSplit = stackTrace.split("\n\t");
            StringBuilder sb = new StringBuilder();
            if (stackTraceSplit.length > 0) {
                for (String sts : stackTraceSplit) {
                    if (
                            !sts.contains("FastClassBySpringCGLIB")
                                    && !sts.contains("cglib")
                                    && !sts.contains("CglibAopProxy")
                                    && !sts.contains("NativeMethodAccessorImpl")
                                    && !sts.contains("AbstractPipeline")
                                    && !sts.contains("ReferencePipeline")
                                    && !sts.contains("GeneratedMethodAccessor")
                                    && !sts.contains("ReflectiveMethodInvocation")
                                    && !sts.contains("Preconditions")
                                    && !sts.contains("Optional")
                                    && !sts.contains("ArrayList")
                                    && !sts.contains("HamExecutorService")
                                    && !sts.contains("ThreadPoolExecutor")
                                    && !sts.contains("BigDecimal")
                                    && !sts.contains("AssertUtil")
                                    && !sts.contains("JoinPointImpl")
                                    && !sts.contains("RpcInvokerTemplate")
                                    && !sts.contains("ArrayList")
                                    && !sts.contains("AjcClosure")
                                    && !sts.contains("InnerMessageListener")
                                    && !sts.contains("CatTransactionManager")
                    ) {
                        if(sts.contains("DependencyException")){
                            String[] split = sts.split("DependencyException");
                            if(split.length >= 1){
                                sb.append("DependencyException").append(split[1]).append("\n\t");
                            }
                        }else{
                            String[] split = sts.split("\\.");
                            int length = split.length;
                            if (length >= 3) {
                                sb.append(split[length - 3]).append(".").append(split[length - 2]).append(".").append(split[length - 1]).append("\n\t");
                            }
                        }
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }
}
