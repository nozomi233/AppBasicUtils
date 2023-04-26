package com.zhulang.basic.utils.mq;

/**
 * @Author zhulang
 * @Date 2023-04-25
 **/
public enum MsgRetryStatus {


    SUCCEED(-1), FAILURE(0), RETRY(0), RETRY_1S(1), RETRY_5S(2),
    RETRY_10S(3), RETRY_30S(4), RETRY_1M(5), RETRY_2M(6), RETRY_3M(7),
    RETRY_4M(8), RETRY_5M(9), RETRY_6M(10), RETRY_7M(11), RETRY_8M(12),
    RETRY_9M(13), RETRY_10M(14), RETRY_20M(15), RETRY_30M(16), RETRY_1H(17), RETRY_2H(18),
    RETRY_1D(19), RETRY_3D(20),  RETRY_7D(21), RETRY_14D(22), RETRY_21D(23),  RETRY_28D(24),
    RETRY_35D(25);

    int level;

    MsgRetryStatus(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

