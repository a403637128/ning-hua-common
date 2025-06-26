package com.ninghua.common.lock.config;

import com.ninghua.common.lock.executor.RedissonLockExecutor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 11:33
 **/
@Configuration
@ConditionalOnClass(Redisson.class)
public class RedissonLockAutoConfiguration {

    @Bean
    @Order(100)
    public RedissonLockExecutor redissonLockExecutor(RedissonClient redissonClient) {
        return new RedissonLockExecutor(redissonClient);
    }

}
