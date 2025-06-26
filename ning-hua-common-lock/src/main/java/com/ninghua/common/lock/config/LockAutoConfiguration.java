package com.ninghua.common.lock.config;

import com.ninghua.common.lock.*;
import com.ninghua.common.lock.aop.Lock4jMethodInterceptor;
import com.ninghua.common.lock.aop.LockAnnotationAdvisor;
import com.ninghua.common.lock.aop.LockOpsInterceptor;
import com.ninghua.common.lock.core.LockExecutor;
import com.ninghua.common.lock.properties.Lock4jProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * 分布式锁自动配置器
 * @Author Derek.Fung
 * @Date 2025/6/24 14:45
 **/
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class LockAutoConfiguration {


    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Lock4jProperties lock4jProperties() {
        return new Lock4jProperties();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(MethodBasedExpressionEvaluator.class)
    @Bean
    public SpelMethodBasedExpressionEvaluator methodBasedExpressionEvaluator() {
        return new SpelMethodBasedExpressionEvaluator();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockKeyBuilder lockKeyBuilder(MethodBasedExpressionEvaluator methodBasedExpressionEvaluator) {
        return new DefaultLockKeyBuilder(methodBasedExpressionEvaluator);
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public DefaultLockFailureStrategy defaultLockFailureStrategy() {
        return new DefaultLockFailureStrategy();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockTemplate lockTemplate(List<LockExecutor> executors, Lock4jProperties properties) {
        LockTemplate lockTemplate = new LockTemplate();
        lockTemplate.setProperties(properties);
        lockTemplate.setExecutors(executors);
        return lockTemplate;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean(Lock4jMethodInterceptor.class)
    public LockOpsInterceptor conditionalLockOpsInterceptor(
            MethodBasedExpressionEvaluator methodBasedExpressionEvaluator,
            Lock4jProperties lock4jProperties, LockTemplate lockTemplate) {
        return new LockOpsInterceptor(methodBasedExpressionEvaluator, lockTemplate, lock4jProperties);
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockAnnotationAdvisor lockAnnotationAdvisor(Lock4jMethodInterceptor lockInterceptor) {
        return new LockAnnotationAdvisor(lockInterceptor, Ordered.HIGHEST_PRECEDENCE);
    }
}
