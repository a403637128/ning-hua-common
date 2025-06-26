package com.ninghua.common.lock;

import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * 基于方法调用的表达式计算器
 * @Author Derek.Fung
 * @Date 2025/6/24 17:37
 **/
public interface MethodBasedExpressionEvaluator {

    /**
     * 执行表达式，返回执行结果
     *
     * @param method 方法
     * @param arguments 调用参数
     * @param expression 表达式
     * @param resultType 返回值类型
     * @param variables 表达式中的变量
     * @return 表达式执行结果
     */
    <T> T getValue(
            Method method, Object[] arguments, String expression, Class<T> resultType, @NonNull Map<String, Object> variables);

    /**
     * 执行表达式，返回执行结果
     *
     * @param method 方法
     * @param arguments 调用参数
     * @param expression 表达式
     * @param resultType 返回值类型
     * @return 表达式执行结果
     */
    default <T> T getValue(
            Method method, Object[] arguments, String expression, Class<T> resultType) {
        return getValue(method, arguments, expression, resultType, Collections.emptyMap());
    }
}
