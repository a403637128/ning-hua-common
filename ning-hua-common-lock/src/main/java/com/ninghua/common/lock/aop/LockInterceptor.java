package com.ninghua.common.lock.aop;

import com.ninghua.common.lock.LockFailureStrategy;
import com.ninghua.common.lock.LockInfo;
import com.ninghua.common.lock.LockKeyBuilder;
import com.ninghua.common.lock.LockTemplate;
import com.ninghua.common.lock.annotation.Lock4j;
import com.ninghua.common.lock.properties.Lock4jProperties;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 14:01
 **/
@Slf4j
@RequiredArgsConstructor
public class LockInterceptor implements Lock4jMethodInterceptor, InitializingBean {

    private final Map<Class<? extends LockKeyBuilder>, LockKeyBuilder> keyBuilderMap = new LinkedHashMap<>();
    private final Map<Class<? extends LockFailureStrategy>, LockFailureStrategy> failureStrategyMap = new LinkedHashMap<>();

    private final LockTemplate lockTemplate;
    private LockOperation primaryLockOperation;

    private final Lock4jProperties lock4jProperties;

    private final List<LockKeyBuilder> keyBuilders;

    private final List<LockFailureStrategy> failureStrategies;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //fix 使用其他aop组件时,aop切了两次.
        Class<?> cls = AopProxyUtils.ultimateTargetClass(invocation.getThis());
        if (!cls.equals(invocation.getThis().getClass())) {
            return invocation.proceed();
        }
        Lock4j lock4j = AnnotatedElementUtils.findMergedAnnotation(invocation.getMethod(),Lock4j.class);
        LockInfo lockInfo = null;
        try {
            LockOperation lockOperation = buildLockOperation(lock4j);
            String prefix = lock4jProperties.getLockKeyPrefix() + ":";
            prefix += StringUtils.hasText(lock4j.name()) ? lock4j.name() :
                    invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
            String key = prefix + "#" + lockOperation.lockKeyBuilder.buildKey(invocation, lock4j.keys());
            lockInfo = lockTemplate.lock(key, lock4j.expire(), lock4j.acquireTimeout(), lock4j.executor());
            if (null != lockInfo) {
                return invocation.proceed();
            }
            // lock failure
            lockOperation.lockFailureStrategy.onLockFailure(key, invocation.getMethod(), invocation.getArguments());
            return null;
        } finally {
            if (null != lockInfo && !lock4j.autoRelease()) {
                final boolean releaseLock = lockTemplate.releaseLock(lockInfo);
                if (!releaseLock) {
                    log.error("releaseLock fail,lockKey={},lockValue={}", lockInfo.getLockKey(),
                            lockInfo.getLockValue());
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        keyBuilderMap.putAll(keyBuilders.stream().collect(Collectors.toMap(LockKeyBuilder::getClass, x -> x)));
        failureStrategyMap.putAll(failureStrategies.stream().collect(Collectors.toMap(LockFailureStrategy::getClass, x -> x)));
        LockKeyBuilder lockKeyBuilder;
        LockFailureStrategy lockFailureStrategy;
        List<LockKeyBuilder> priorityOrderedLockBuilders = keyBuilders.stream().filter(Ordered.class::isInstance).collect(Collectors.toList());
        if (lock4jProperties.getPrimaryKeyBuilder() != null) {
            lockKeyBuilder = keyBuilderMap.get(lock4jProperties.getPrimaryKeyBuilder());
        } else if (!priorityOrderedLockBuilders.isEmpty()) {
            sortOperation(priorityOrderedLockBuilders);
            lockKeyBuilder = priorityOrderedLockBuilders.get(0);
        } else {
            lockKeyBuilder = keyBuilders.get(0);
        }
        List<LockFailureStrategy> priorityOrderedFailures = failureStrategies.stream().filter(Ordered.class::isInstance).collect(Collectors.toList());
        if (lock4jProperties.getPrimaryFailureStrategy() != null) {
            lockFailureStrategy = failureStrategyMap.get(lock4jProperties.getPrimaryFailureStrategy());
        } else if (!priorityOrderedFailures.isEmpty()) {
            sortOperation(priorityOrderedFailures);
            lockFailureStrategy = priorityOrderedFailures.get(0);
        } else {
            lockFailureStrategy = failureStrategies.get(0);
        }
        primaryLockOperation = LockOperation.builder().lockKeyBuilder(lockKeyBuilder).lockFailureStrategy(lockFailureStrategy).build();
    }

    private LockOperation buildLockOperation(Lock4j lock4j){
        LockKeyBuilder lockKeyBuilder;
        LockFailureStrategy lockFailureStrategy;
        Class<? extends LockFailureStrategy> failStrategy = lock4j.failStrategy();
        Class<? extends LockKeyBuilder> keyBuilderStrategy = lock4j.keyBuilderStrategy();
        if (keyBuilderStrategy == null || keyBuilderStrategy == LockKeyBuilder.class) {
            lockKeyBuilder = primaryLockOperation.lockKeyBuilder;
        } else {
            lockKeyBuilder = keyBuilderMap.get(keyBuilderStrategy);
        }
        if (failStrategy == null || failStrategy == LockFailureStrategy.class) {
            lockFailureStrategy = primaryLockOperation.lockFailureStrategy;
        } else {
            lockFailureStrategy = failureStrategyMap.get(failStrategy);
        }
        return LockOperation.builder().lockKeyBuilder(lockKeyBuilder).lockFailureStrategy(lockFailureStrategy).build();
    }


    @Builder
    private static class LockOperation{
        /**
         * key生成器
         */
        private LockKeyBuilder lockKeyBuilder;
        /**
         * 锁失败策略
         */
        private LockFailureStrategy lockFailureStrategy;
    }

    private void sortOperation(List<?> operations){
        if (operations.size()<=1){
            return;
        }
        operations.sort(OrderComparator.INSTANCE);
    }
}
