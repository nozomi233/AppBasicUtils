package com.zhulang.common.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * redis抽象类，实现类
 * @Author zhulang
 * @Date 2023-04-24
 **/
//@Slf4j
public abstract class BaseCache<R, P> implements ICache<R, P> {

//    private IRedisHelper helper;
//
//    public abstract Key getRedisKey(P p);
//
    /**
     * 限流key
     */
//    public abstract Key getRedisLimitKey(P p);
//
//    public abstract int getRedisLimitExpire(P p);
//
    /**
     * 过期时间
     */
//    public abstract int getRedisKeyExpire(P p);
//
    /**
     * DB读缓存
     */
//    public abstract R fetchPersistModel(P p);
//
//    public abstract R desSerialize(String json);
//
//
//    public BaseCache(IRedisHelper helper) {
//        this.helper = helper;
//    }
//
//    @Override
//    public boolean build(P p, R r) {
//
//        if (Objects.nonNull(r)) {
//            String json = JSON.toJSONString(r);
//            helper.set(getRedisKey(p), json, getRedisKeyExpire(p));
//        } else {
//            helper.set(getRedisLimitKey(p), "true", getRedisLimitExpire(p));
//        }
//        return true;
//    }
//
//    @Override
//    public R load(P p) {
//
//        // 读缓存
//        Key redisKey = getRedisKey(p);
//        String cacheJson = helper.get(redisKey);
//        if (StringUtils.isNotEmpty(cacheJson)) {
//            return desSerialize(cacheJson);
//        }
//        // 检查限流状态
//        Key limitKey = getRedisLimitKey(p);
//        String limitValue = helper.get(limitKey);
//        if (StringUtils.isNotEmpty(limitValue)) {
//            return null;
//        }
//        // 自旋处理
//        String lockKey = RedisKey.cacheSynKey(getRedisKey(p));
//        RedisLock lock = helper.createLock(lockKey);
//        if (!lock.blockAcquireLock(Consts.OP_PROCESS_TIME, Consts.BLOCK_LOCK_TIME)) {
//            return null;
//        }
//        try {
//            // 读db数据、回写缓存
//            R r = fetchPersistModel(p);
//            build(p, r);
//            return r;
//        } finally {
//            try {
//                lock.releaseLock();
//            } catch (Exception e) {
//                log.error(String.format("BaseCache# release lock error,lockKey:%s", lockKey), e);
//            }
//
//        }
//    }
//
//    @Override
//    public boolean remove(P p) {
//
//        Key redisKey = getRedisKey(p);
//        Key limitKey = getRedisLimitKey(p);
//        helper.del(limitKey);
//        helper.del(redisKey);
//        return true;
//    }
}
