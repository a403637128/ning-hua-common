package com.ninghua.common.lock;

import com.ninghua.common.lock.core.LockExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 14:35
 **/
@Data
@AllArgsConstructor
public class LockInfo {
    /**
     * 锁名称
     */
    private String lockKey;

    /**
     * 锁值
     */
    private String lockValue;

    /**
     * 过期时间
     */
    private Long expire;

    /**
     * 获取锁超时时间
     */
    private Long acquireTimeout;

    /**
     * 获取锁次数
     */
    private int acquireCount;

    /**
     * 锁实例
     */
    private Object lockInstance;

    /**
     * 锁执行器
     */
    private LockExecutor lockExecutor;
}
