package com.zhulang.basic.utils.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author zhulang
 * @Date 2023-04-26
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum IDEMPOTENT_POLICY {

    /**
     * 消息本身支持重复消费，不需要进行幂等
     */
    NO_NEED_IDEMPOTENT("不需要幂等"),

    /**
     * 在消费失败的情况下，可以认为消息没有消费，进行重新消费
     * <>使用典型场景：1.消息的处理过程中属于一个事物，消费失败，事物回滚，消息可以重新消费</>
     * <>2. 处理过程中包含本地事物和外部调用，外部调用本身支持幂等</>
     */
    IDEMPOTENT_BY_FRAMEWORK("使用框架提供幂等机制"),


    /**
     * 在消费失败的情况下，不能认为消息没有消费，可能产生部分逻辑执行成功，重试产生脏写
     * 这种场景需要自己保证幂等
     */
    IDEMPOTENT_BY_MYSELF("自己实现的时候保证幂等"),

    ;

    private String desc;


}
