package com.example.school.common.redisson;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/14 17:11
 * description:
 */
public interface DistributedLocker {
    /**
     * 加锁
     *
     * @param lockKey key
     * @return RLock
     */
    RLock lock(String lockKey);

    /**
     * 加锁并设置锁过期时间
     *
     * @param lockKey key
     * @param timeout 超时时间
     * @return RLock
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 加锁并设置锁过期时间，并指定时间格式
     *
     * @param lockKey key
     * @param unit    时间格式
     * @param timeout 超时时间
     * @return RLock
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * tryLock()，马上返回，拿到lock就返回true，不然返回false。
     * 带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false.
     *
     * @param lockKey   key
     * @param unit      时间格式
     * @param waitTime  等待时间
     * @param leaseTime 超时时间
     * @return boolean
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 解锁
     *
     * @param lockKey key
     */
    void unlock(String lockKey);

    /**
     * 解锁
     *
     * @param lock key
     */
    void unlock(RLock lock);
}
