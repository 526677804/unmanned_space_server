package com.yanzu.framework.idempotent.config;

import com.yanzu.framework.idempotent.core.aop.IdempotentAspect;
import com.yanzu.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.yanzu.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.yanzu.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.yanzu.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import com.yanzu.framework.redis.config.MazongjianRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = MazongjianRedisAutoConfiguration.class)
public class MazongjianIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
