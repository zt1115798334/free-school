package com.example.school.common.config.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/14 17:03
 * description: 分布式锁配置
 */
@AllArgsConstructor
@Slf4j
@Configuration
public class RedissonConfig {

    private final RedisProperties redisProperties;
    //支持单机，主从，哨兵，集群等模式

    /**
     * 单机模式
     *
     * @return RedissonClient
     */
    @Bean
    RedissonClient redissonSingle() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setDatabase(0)
                .setPassword(redisProperties.getPassword());
        return Redisson.create(config);
    }


}
