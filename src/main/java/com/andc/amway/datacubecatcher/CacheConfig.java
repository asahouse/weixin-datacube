package com.andc.amway.datacubecatcher;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CacheConfig extends CachingConfigurerSupport {


//    @Bean
//    public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate) {
//        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        return cacheManager;
//    }

//    //改用redisson进行分布式缓存
//    /**
//     * 缓存管理器.
//     * @return
//     */
//    @Bean
//    public CacheManager cacheManager() {
//        CacheManager cacheManager = new CaffeineCacheManager();
//        return cacheManager;
//    }
}
