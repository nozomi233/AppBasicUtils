package com.zhulang.basic.utils.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Collection;
import java.util.List;
/**
 * @Author zhulang
 * @Date 2023-04-26
 **/
public interface MessageListener {

    RuntimeException exception = new RuntimeException("illegal messageListener type, should correct rocketmqMessageListener or kafkaMessageListener");

    /**
     * 消息通用默认接口
     * -- Easy模式
     * @param msg 消息
     * @return
     */
    public default MsgRetryStatus onMessage(ConsumeMessage msg) {
        return MsgRetryStatus.SUCCEED;
    }

    /**
     * RocketMQ原生消息批量消费
     *
     * Properties properties = new Properties();
     * sproperties.put("isEasy","false");
     * @param msgs 消息集合
     * @return
     */
    @Deprecated
    public default MsgRetryStatus onMessage(List<MessageExt> msgs) {
        return MsgRetryStatus.SUCCEED;
    }

    /**
     * Kafka原生消息批量消费
     *
     * Properties properties = new Properties();
     * sproperties.put("isEasy","false");
     * @param msgs 消息集合
     * @return
     */
    public default MsgRetryStatus onMessage(Collection<ConsumerRecord> msgs) {
        return MsgRetryStatus.SUCCEED;
    }

}
