package com.example.school.shiro.shiro.config;

import com.example.school.shiro.shiro.factory.StatelessWebSubjectFactory;
import com.example.school.shiro.shiro.filter.ShiroFilterChainManager;
import com.example.school.shiro.shiro.realm.AModularRealmAuthenticator;
import com.example.school.shiro.shiro.realm.RealmManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:07
 * description:
 */
@Slf4j
@Configuration
public class ShiroBaseConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroFilterChainManager filterChainManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/401");
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filterChainManager.initGetFilters(cacheManager()));
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainManager.initGetFilterChain());
        log.info("Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(RealmManager realmManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(new AModularRealmAuthenticator());
        securityManager.setRealms(realmManager.initGetRealm());
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(Boolean.FALSE);
        StatelessWebSubjectFactory subjectFactory = new StatelessWebSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);
//        securityManager.setCacheManager(cacheManager());  //注释掉 缓存会导致权限不会刷新 -- 以后想到办法后再修改。
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return RedisManager
     */
    private RedisManager redisManager() {
        Duration timeout = redisProperties.getTimeout();
        long timeoutMillis = timeout.toMillis();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        // 配置缓存过期时间
        redisManager.setExpire(1800);
        redisManager.setTimeout((int) timeoutMillis);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return RedisCacheManager
     */
    private RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

}
