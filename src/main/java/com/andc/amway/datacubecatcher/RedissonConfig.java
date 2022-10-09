package com.andc.amway.datacubecatcher;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class RedissonConfig {

    @Autowired
    private Environment env;

    @Bean
    public CacheManager cacheManager() throws IOException {
        Long ttl = 7*24*60*60*1000L; //1week
        Long maxIdleTime = 2*24*60*1000L;//2day

        CacheConfig commonConfig = new CacheConfig(ttl, maxIdleTime);

        Map<String, CacheConfig> configMap = new HashMap<>();
//            configMap.put("CacheDSLCountCreator", commonConfig);

        return new RedissonSpringCacheManager(redissonClient(), configMap);
    }

    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient() throws IOException {
        String[] profiles = env.getActiveProfiles();
        String profile = "";
        if(profiles.length > 0) {
            profile = "-" + profiles[0];
        }
        return Redisson.create(
                Config.fromYAML(new ClassPathResource("redisson" + profile + ".yml").getInputStream())
        );
    }
}
