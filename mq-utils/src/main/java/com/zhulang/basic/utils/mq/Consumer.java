package com.zhulang.basic.utils.mq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author zhulang
 * @Date 2023-04-26
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Consumer {

    /**
     * 主题
     */
    String topicName() default "";

    /**
     * 消费者
     */
    String consumerName();

    /**
     * tag
     */
    String[] tags() default {};

    /**
     * 重试告警阈值
     */
    int alertThreshold() default 4;

    /**
     * 停止告警阈值，超过此次数一般需要人工干预，再告警也没意义
     */
    int stopAlertThreshold() default 5;

    /**
     * 失败重试次数 , 重试多少次后不再重试
     */
    int retryCount() default -1;

    /**
     * 是否顺序消费
     */
    boolean isOrderly() default false;

    /**
     * 重试多少次后调用失败处理器
     */
    int retryCountToCallFailHandler() default -1;

    /**
     * 延迟级别
     */
    MsgRetryStatus delayLevel() default MsgRetryStatus.FAILURE;


    /**
     * 幂等策略
     */
    IDEMPOTENT_POLICY idempotentPolicy() default IDEMPOTENT_POLICY.NO_NEED_IDEMPOTENT;

    /**
     * 消费线程的数量
     */
    int consumeThreadMin() default 10;

    /**
     * 消费线程的数量
     */
    int consumeThreadMax() default 20;

    /**
     * MQ类型
     */
    MQ_TYPE type() default MQ_TYPE.ROCKET_MQ;


    boolean kafkaExceptionReport() default false;
}
