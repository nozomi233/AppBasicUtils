package com.zhulang.basic.utils.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.basic.utils.common.DefaultExceptionFilter;
import com.zhulang.basic.utils.common.Environment;
import com.zhulang.basic.utils.common.ExceptionFilter;
import com.zhulang.basic.utils.common.StackTraceUtils;
import com.zhulang.basic.utils.common.constants.Constants;
import com.zhulang.basic.utils.common.dingding.DingTalkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhulang
 * @Date 2023-04-26
 **/
@Slf4j
@Configurable
public abstract class AbstractConsumerTemplate<T> implements ApplicationListener<ApplicationStartedEvent> {

    public static final String MDC_TITLE_ID = "title";

    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(32, 32, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.DiscardPolicy());

    @Value("${dingDingToken:ad7905d8c558a98dd4f696b381e74207a75a0f3ff48fd78f62af603815e3aa47}")
    private String dingDingToken;

    @Value("${bizDingDingToken:ad7905d8c558a98dd4f696b381e74207a75a0f3ff48fd78f62af603815e3aa47}")
    private String bizDingDingToken;

//    涉及redis lock省略
//    @Resource
//    private IRedisHelper redisHelper;

    private DingTalkService dingTalkService = new DingTalkService();

    private ExceptionFilter exceptionFilter = new DefaultExceptionFilter();

    @Value("${exceptionReport:true}")
    private Boolean exceptionReport;

    private final Consumer consumerAnnotation;

    private final String className;

    private final Class<T> parameterizedType;

    public AbstractConsumerTemplate() {
        className = this.getClass().getSimpleName();
        consumerAnnotation = this.getClass().getAnnotation(Consumer.class);
        parameterizedType = getGenericClass();
    }

    /**
     * 消息处理
     *
     * @param t
     * @return
     */
    public abstract void process(T t);

    /**
     * 超过一定重试失败次数后处理器
     *
     * @param t
     */
    public void retryFailHandler(T t, Exception e) throws Exception {

    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Properties properties = new Properties();
        // 设置消费线程的数量
        properties.setProperty("consumeThreadMin", String.valueOf(consumerAnnotation.consumeThreadMin()));
        properties.setProperty("consumeThreadMax", String.valueOf(consumerAnnotation.consumeThreadMax()));
        if (consumerAnnotation.isOrderly()) {
            // 顺序消费
            properties.setProperty("isOrderly", "true");
        }
        if (consumerAnnotation.tags().length > 0) {
            // todo zl 带tag消息订阅,此处以log mock
//            Hms.subscribe(consumerAnnotation.consumerName(), new HashSet<>(Arrays.asList(consumerAnnotation.tags())), new InnerMessageListener(), properties);
            log.info("发送消息");
        } else {
            // todo zl 不带tag消息订阅,此处以log mock
//            Hms.subscribe(consumerAnnotation.consumerName(), new InnerMessageListener(), properties);
            log.info("发送消息");
        }
    }

    class InnerMessageListener implements MessageListener {

        @Override
        public MsgRetryStatus onMessage(ConsumeMessage msg) {
            T t = null;
//            RedisLock lock = null;
            try {
                MDC.put(MDC_TITLE_ID, RandomStringUtils.randomAlphanumeric(10));

                t = parseMessage(msg);

                if (t == null) {
                    return MsgRetryStatus.SUCCEED;
                }

                if (MQ_TYPE.ROCKET_MQ == consumerAnnotation.type()) {
                    if (IDEMPOTENT_POLICY.IDEMPOTENT_BY_FRAMEWORK.equals(consumerAnnotation.idempotentPolicy())) {
//                        lock = redisHelper.createLock(msg.getMsgId() + "_" + consumerAnnotation.consumerName());
//                        boolean acquireLock = lock.acquireLock(TimeUnit.HOURS.toSeconds(30));
//                        if (!acquireLock) {
                            log.info("duplication msg , msgId={}", msg.getMsgId());
                            return MsgRetryStatus.SUCCEED;
//                        }
                    }
                }

                process(t);

                log.info("consume success,msgId={},key={}", msg.getMsgId(), msg.getKey());
                return MsgRetryStatus.SUCCEED;
            } catch (Exception e) {
                log.error("consume msg error", e);
                if (MQ_TYPE.ROCKET_MQ == consumerAnnotation.type()) {
//                    if (lock != null) {
//                        // 释放分布式锁
//                        lock.releaseLock();
//                    }

                    int rct = getReconsumeTimes(msg);

                    // 异常上报
                    report(msg, e, rct);

                    // 失败补偿
                    MsgRetryStatus handlerResult = callFailHandler(t, e, rct);
                    if (handlerResult != null) {
                        return handlerResult;
                    }

                    if (consumerAnnotation.retryCount() >= 0 && rct >= consumerAnnotation.retryCount()) {
                        log.info("discard msg,msgId={},key={}", msg.getMsgId(), msg.getKey());
                        return MsgRetryStatus.SUCCEED;
                    }

                    // 异常不重试
                    boolean filterMsgException = exceptionFilter.filterMsgException(e);
                    if (filterMsgException) {
                        return MsgRetryStatus.SUCCEED;
                    }
                }

                if (MQ_TYPE.KAFKA == consumerAnnotation.type()) {
                    try {
                        if (consumerAnnotation.kafkaExceptionReport()) {
                            doReport(msg, e, 0);
                        }
                        retryFailHandler(t, e);
                    } catch (Exception ex) {
                        log.error("call retryFailHandler error", ex);
                    }

                }

                return consumerAnnotation.delayLevel();
            } finally {
                MDC.remove(MDC_TITLE_ID);
            }
        }


        private MsgRetryStatus callFailHandler(T t, Exception e, Integer rct) {
            int retryCountToCallFailHandler = consumerAnnotation.retryCountToCallFailHandler();
            if (retryCountToCallFailHandler > 0 && rct >= retryCountToCallFailHandler && t != null) {
                try {
                    log.info("call retryFailHandler");
                    retryFailHandler(t, e);
                } catch (Exception retryException) {
                    log.error("call retryFailHandler error", retryException);
                    return consumerAnnotation.delayLevel();
                }
                return MsgRetryStatus.SUCCEED;
            }
            return null;
        }
    }

    private void report(ConsumeMessage msg, Exception exception, int rct) {
        try {
            if (rct >= consumerAnnotation.alertThreshold() && rct <= consumerAnnotation.stopAlertThreshold()) {
                doReport(msg, exception, rct);
            }
        } catch (Exception e) {
            log.error("report error", e);
        }
    }

    private void doReport(ConsumeMessage msg, Exception exception, int rct) {
        String innerId = MDC.get(MDC_TITLE_ID);
        StringBuilder sb = new StringBuilder();
        sb.append("ip=").append(Environment.IP).append("&ensp;");
        sb.append("env=").append(Environment.ENV).append("&ensp;");
        sb.append("app_id=").append(Environment.APP_ID).append("\n");
        sb.append("Class=").append(className).append("\n");
        sb.append("topic=").append(consumerAnnotation.topicName()).append("\n");
        sb.append("consume=").append(consumerAnnotation.consumerName()).append("\n");
        sb.append("KEY=").append(msg.getKey()).append("\n");
        sb.append("msgId=").append(msg.getMsgId()).append("\n");
        if (rct > 0) {
            sb.append("消息消费失败,重试次数:").append(rct).append("\n");
        }
        sb.append(StackTraceUtils.getStackTrace(exception)).append("\n");

        String hlogUrl = null;
        long now = System.currentTimeMillis();
        if ("pro".equals(Environment.ENV)) {
            hlogUrl = String.format(Constants.PRO_HLOG, Environment.APP_ID, now - 5000, now + 5000, innerId);
        } else if ("fat".equals(Environment.ENV)) {
            hlogUrl = String.format(Constants.FAT_HLOG, Environment.APP_ID, now - 5000, now + 5000, innerId);
        } else if ("uat".equals(Environment.ENV)) {
            hlogUrl = String.format(Constants.UAT_HLOG, Environment.APP_ID, now - 5000, now + 5000, innerId);
        } else if ("pre".equals(Environment.ENV)) {
            hlogUrl = String.format(Constants.PRE_HLOG, Environment.APP_ID, now - 5000, now + 5000, innerId);
        }
        sb.append("[hlog]").append("(").append(hlogUrl).append(")");

        String text = sb.toString().replace("\n", "  \n");
        boolean filterException = exceptionFilter.filterException(exception);
        if (filterException) {
            if (BooleanUtils.isTrue(exceptionReport)) {
                THREAD_POOL.execute(() -> dingTalkService.send(text, "业务报警", bizDingDingToken, null));
            }
        } else {
            THREAD_POOL.execute(() -> dingTalkService.send(text, "业务报警", dingDingToken, null));
        }
    }


    /**
     * 消息解析
     * public: 当之类需重新的时候，可以进行覆盖
     * @param msg
     * @return
     */
    public T parseMessage(ConsumeMessage msg) {
        byte[] messageBytes = msg.getPayload();
        String messageString = new String(messageBytes, StandardCharsets.UTF_8);
        log.info("{},msgId={},key={},topic={},consumer={},properties={} ,message body = {} ", className, msg.getMsgId(), msg.getKey(), consumerAnnotation.topicName(), consumerAnnotation.consumerName(), JSON.toJSONString(msg.getProperties()), messageString);

        return JSONObject.parseObject(messageString, parameterizedType);
    }


    private Class<T> getGenericClass() {
        Class clazz = getClass();
        while (clazz != null) {
            if (clazz.getSuperclass() == AbstractConsumerTemplate.class) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        assert clazz != null;

        Type genType = clazz.getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class) params[0];
    }


    private int getReconsumeTimes(ConsumeMessage msg) {

        Object reconsumeTimes = msg.getProperties().get("reconsumeTimes");
        if (reconsumeTimes instanceof String) {
            return Integer.parseInt(String.valueOf(reconsumeTimes));
        }
        return 0;
    }
}
