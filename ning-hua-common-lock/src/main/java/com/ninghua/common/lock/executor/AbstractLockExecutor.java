package com.ninghua.common.lock.executor;

import com.ninghua.common.lock.core.LockExecutor;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 11:51
 **/
public abstract class AbstractLockExecutor<T> implements LockExecutor<T> {

    protected T obtainLockInstance(boolean locked, T lockInstance) {
        return locked ? lockInstance : null;
    }
}
