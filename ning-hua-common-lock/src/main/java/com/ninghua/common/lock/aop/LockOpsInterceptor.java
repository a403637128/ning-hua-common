package com.ninghua.common.lock.aop;

import com.ninghua.common.lock.*;
import com.ninghua.common.lock.annotation.Lock4j;
import com.ninghua.common.lock.properties.Lock4jProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * 基于{@link Lock4j}注解的锁操作拦截器
 * @Author Derek.Fung
 * @Date 2025/6/24 17:00
 **/
@Slf4j
public class LockOpsInterceptor extends AbstractConditionalLockChainInterceptor {

    private final LockTemplate lockTemplate;
    protected final Lock4jProperties lock4jProperties;

    public LockOpsInterceptor(
            MethodBasedExpressionEvaluator methodBasedExpressionEvaluator,
            LockTemplate lockTemplate, Lock4jProperties lock4jProperties) {
        super(methodBasedExpressionEvaluator);
        this.lockTemplate = lockTemplate;
        this.lock4jProperties = lock4jProperties;
    }

    @NonNull
    @Override
    protected LockOps initDefaultLockOps() {
        LockKeyBuilder lockKeyBuilder = obtainComponent(LockKeyBuilder.class, lock4jProperties.getPrimaryKeyBuilder());
        LockFailureStrategy lockFailureStrategy = obtainComponent(LockFailureStrategy.class, lock4jProperties.getPrimaryFailureStrategy());
        return new LockOpsImpl(null)
                .setLockKeyBuilder(lockKeyBuilder)
                .setLockFailureStrategy(lockFailureStrategy);
    }

    @Override
    protected Object doLock(LockOps lockOps, MethodInvocation invocation) throws Throwable {
        Lock4j annotation = lockOps.getAnnotation();
        LockInfo lockInfo = null;
        try {
            String key = resolveKey(invocation, lockOps);
            lockInfo = lockTemplate.lock(key, annotation.expire(), annotation.acquireTimeout(), annotation.executor());
            if (Objects.nonNull(lockInfo)) {
                log.debug("Lock success, lockKey={}, lockValue={}", lockInfo.getLockKey(), lockInfo.getLockValue());
                return invocation.proceed();
            }
            log.debug("Lock failure, lockKey={}", key);
            // lock failure
            lockOps.getLockFailureStrategy()
                    .onLockFailure(key, invocation.getMethod(), invocation.getArguments());
            return null;
        } finally {
            doUnlock(lockInfo, annotation);
        }
    }

    private void doUnlock(@Nullable LockInfo lockInfo, Lock4j annotation) {
        if (Objects.isNull(lockInfo) || !annotation.autoRelease()) {
            return;
        }
        final boolean releaseLock = lockTemplate.releaseLock(lockInfo);
        if (!releaseLock) {
            log.error("Release lock fail, lockKey={}, lockValue={}", lockInfo.getLockKey(),
                    lockInfo.getLockValue());
        }
        log.debug("Release lock success, lockKey={}, lockValue={}", lockInfo.getLockKey(),
                lockInfo.getLockValue());
    }

    private String resolveKey(MethodInvocation invocation, LockOps lockOps) {
        String prefix = lock4jProperties.getLockKeyPrefix() + ":";
        Method method = invocation.getMethod();
        Lock4j annotation = lockOps.getAnnotation();
        prefix += StringUtils.hasText(annotation.name()) ? annotation.name() :
                method.getDeclaringClass().getName() + method.getName();
        String key = prefix + "#" + lockOps.getLockKeyBuilder().buildKey(invocation, annotation.keys());
        if (log.isDebugEnabled()) {
            log.debug("generate lock key [{}] for invocation of [{}]", key, method);
        }
        return key;
    }

    private <C> C obtainComponent(Class<C> type, @Nullable Class<? extends C> defaultType) {
        // TODO 支持根据 beanName 获取相应组件
        if (Objects.nonNull(defaultType)) {
            return applicationContext.getBean(defaultType);
        }
        Collection<C> components = applicationContext.getBeansOfType(type).values();
        return components.stream()
                .min(AnnotationAwareOrderComparator.INSTANCE)
                .orElseThrow(() -> new IllegalArgumentException("No component of type " + type.getName() + " found"));
    }
}
