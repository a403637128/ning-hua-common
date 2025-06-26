package com.ninghua.common.lock.config;

import com.ninghua.common.lock.executor.RedisTemplateLockExecutor;
import com.ninghua.common.lock.properties.Lock4jProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 16:31
 **/
@Configuration
@ConditionalOnClass(RedisOperations.class)
public class RedisTemplateLockAutoConfiguration {

    @Bean
    @Order(200)
    public RedisTemplateLockExecutor redisTemplateLockExecutor(StringRedisTemplate stringRedisTemplate, Lock4jProperties lock4jProperties) {
        return new RedisTemplateLockExecutor(stringRedisTemplate,lock4jProperties);
    }
}
