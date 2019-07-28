package com.example.school.common.redis;

import com.alibaba.fastjson.JSONObject;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/12 11:29
 * description: redis业务层
 */
public interface RedisService {


    /**
     * 根据key查询
     *
     * @param key key
     * @return Optional<JSONObject>
     */
    Optional<JSONObject> get(String key);

    /**
     * 保存 包含过期时间 默认 30分钟
     *
     * @param key   key
     * @param value value
     */
    void setContainExpire(String key, JSONObject value);

    /**
     * 保存 包含过期时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void setContainExpire(String key, JSONObject value, long timeout, TimeUnit unit);

    /**
     * 保存 不包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpire(String key, JSONObject value);

    /**
     * keys
     *
     * @param key key
     * @return Set<String>
     */
    Set<String> keys(String key);

    /**
     * flushDB
     */
    void flushDB();

    /**
     * dbSize
     */
    Long dbSize();

    /**
     * 删除
     *
     * @param key key
     * @return boolean
     */
    Boolean delete(String key);

    /**
     * 这里是收到通道的消息之后执行的方法
     *
     * @param message msg
     */
    void receiveMessage(String message);

    /**
     * 获取指定key的过期时间
     * @param key   key值
     * @return 剩余过期时间毫秒
     */
    Long getExpire(String key);
}
