package com.ninghua.common.lock;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 分布式锁Key生成器
 * @Author Derek.Fung
 * @Date 2025/6/24 18:15
 **/
@RequiredArgsConstructor
public class DefaultLockKeyBuilder implements LockKeyBuilder{

    private final MethodBasedExpressionEvaluator methodBasedExpressionEvaluator;
    private static final String EMPTY_KEY = "";

    @Override
    public String buildKey(MethodInvocation invocation, String[] definitionKeys) {
        return ObjectUtils.isEmpty(definitionKeys) ?
                EMPTY_KEY : getSpelDefinitionKey(definitionKeys,invocation);
    }

    protected String getSpelDefinitionKey(String[] definitionKeys, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        return Stream.of(definitionKeys)
                .filter(StringUtils::hasText)
                .map(k -> methodBasedExpressionEvaluator.getValue(method, arguments, k, String.class))
                .collect(Collectors.joining("."));
    }
}
