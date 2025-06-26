package com.ninghua.common.lock.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 11:34
 **/
@Slf4j
@RequiredArgsConstructor
public class RedissonLockExecutor extends AbstractLockExecutor<RLock> {

    private final RedissonClient redissonClient;

    @Override
    public boolean renewal() {
        return true;
    }
    @Override
    public RLock acquire(String lockKey, String lockValue, long expire, long acquireTimeout) {
        try {
            final RLock lockInstance = redissonClient.getLock(lockKey);
            final boolean locked = lockInstance.tryLock(acquireTimeout, expire, TimeUnit.MILLISECONDS);
            return obtainLockInstance(locked, lockInstance);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public boolean releaseLock(String key, String value, RLock lockInstance) {
        if (lockInstance.isHeldByCurrentThread()) {
            try {
                lockInstance.unlockAsync().get();
                return true;
            } catch (ExecutionException | InterruptedException e) {
                return false;
            }
        }
        return false;
    }
}
