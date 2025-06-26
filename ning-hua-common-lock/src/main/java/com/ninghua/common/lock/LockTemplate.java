package com.ninghua.common.lock;

import cn.hutool.core.util.IdUtil;
import com.ninghua.common.core.exception.LockException;
import com.ninghua.common.lock.core.LockExecutor;
import com.ninghua.common.lock.properties.Lock4jProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 锁模板方法
 * @Author Derek.Fung
 * @Date 2025/6/24 14:11
 **/
@Slf4j
public class LockTemplate implements InitializingBean {

    private final Map<Class<? extends LockExecutor>, LockExecutor> executorMap = new LinkedHashMap<>();
    @Setter
    private Lock4jProperties properties;
    @Setter
    private List<LockExecutor> executors;
    private LockExecutor primaryExecutor;

    public LockInfo lock(String key) {
        return lock(key, 0, -1);
    }

    public LockInfo lock(String key, long expire, long acquireTimeout) {
        return lock(key, expire, acquireTimeout, null);
    }


    /**
     * 加锁方法
     *
     * @param key            锁key 同一个key只能被一个客户端持有
     * @param expire         过期时间(ms) 防止死锁
     * @param acquireTimeout 尝试获取锁超时时间(ms)
     * @param executor       执行器
     * @return 加锁成功返回锁信息 失败返回null
     */
    public LockInfo lock(String key, long expire, long acquireTimeout, Class<? extends LockExecutor> executor) {
        acquireTimeout = acquireTimeout < 0 ? properties.getAcquireTimeout() : acquireTimeout;
        long retryInterval = properties.getRetryInterval();
        LockExecutor lockExecutor = obtainExecutor(executor);
        log.debug(String.format("use lock class: %s", lockExecutor.getClass()));
        expire = !lockExecutor.renewal() && expire <= 0 ? properties.getExpire() : expire;
        int acquireCount = 0;
        String value = IdUtil.simpleUUID();
        long start = System.currentTimeMillis();
        try {
            do {
                acquireCount++;
                Object lockInstance = lockExecutor.acquire(key, value, expire, acquireTimeout);
                if (null != lockInstance) {
                    return new LockInfo(key, value, expire, acquireTimeout, acquireCount, lockInstance,
                            lockExecutor);
                }
                TimeUnit.MILLISECONDS.sleep(retryInterval);
            } while (System.currentTimeMillis() - start < acquireTimeout);
        } catch (InterruptedException e) {
            log.error("lock error", e);
            throw new LockException();
        }
        return null;
    }

    public boolean releaseLock(LockInfo lockInfo) {
        if (null == lockInfo) {
            return false;
        }
        return lockInfo.getLockExecutor().releaseLock(lockInfo.getLockKey(), lockInfo.getLockValue(),
                lockInfo.getLockInstance());
    }

    protected LockExecutor obtainExecutor(Class<? extends LockExecutor> clazz) {
        if (null == clazz || clazz == LockExecutor.class) {
            return primaryExecutor;
        }
        final LockExecutor lockExecutor = executorMap.get(clazz);
        Assert.notNull(lockExecutor, String.format("can not get bean type of %s", clazz));
        return lockExecutor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.isTrue(properties.getAcquireTimeout() >= 0, "tryTimeout must least 0");
        Assert.isTrue(properties.getExpire() >= -1, "expireTime must lease -1");
        Assert.isTrue(properties.getRetryInterval() >= 0, "retryInterval must more than 0");
        Assert.hasText(properties.getLockKeyPrefix(), "lock key prefix must be not blank");
        Assert.notEmpty(executors, "executors must have at least one");

        for (LockExecutor executor : executors) {
            executorMap.put(executor.getClass(), executor);
        }

        final Class<? extends LockExecutor> primaryExecutor = properties.getPrimaryExecutor();
        if (null == primaryExecutor) {
            this.primaryExecutor = executors.get(0);
        } else {
            this.primaryExecutor = executorMap.get(primaryExecutor);
            Assert.notNull(this.primaryExecutor, "primaryExecutor must be not null");
        }
    }
}
