package com.example.school.common.config.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 1/4/2019 5:57 PM
 * description:
 */
@AllArgsConstructor
@Slf4j
@Configuration
public class JedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public JedisPool redisPoolFactory() {
        int maxIdle = redisProperties.getJedis().getPool().getMaxIdle();
        long maxWaitMillis = redisProperties.getJedis().getPool().getMaxWait().toMillis();
        Long timeoutMillis = redisProperties.getTimeout().toMillis();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        redis.clients.jedis.JedisPoolConfig jedisPoolConfig = new redis.clients.jedis.JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return new JedisPool(jedisPoolConfig, host, port, timeoutMillis.intValue(),password);
    }
}
