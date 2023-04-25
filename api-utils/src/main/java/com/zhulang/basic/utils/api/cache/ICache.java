package com.zhulang.common.common.cache;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public interface ICache<R, P> {

    /**
     * 构建
     * @param r
     * @return
     */
    boolean build(P p,R r);

    /**
     * 获取
     * @param p
     * @return
     */
    R load(P p);

    /**
     * 清除
     * @param p
     * @return
     */
    boolean remove(P p);
}
