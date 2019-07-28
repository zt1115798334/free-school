package com.example.school.shiro.aop;

import com.example.school.common.redisson.DistributedLocker;
import com.example.school.shiro.base.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/4/9 11:04
 * description: 缓存锁
 */
@Slf4j
@AllArgsConstructor
public abstract class AbsDistributedLockAspect implements CurrentUser {

    private final DistributedLocker distributedLocker;

    protected abstract void aopPointCut(DistributedLock lock);

    @Around("aopPointCut(lock)")
    public Object doAround(ProceedingJoinPoint point, DistributedLock lock) throws Throwable {
        String name = point.getSignature().getName();
        Class<?> classTarget = point.getTarget().getClass();
        boolean flag;
        String lockKey = lock.lockNamePre() + ":" + classTarget.getSimpleName() + "_" + name + "_" + getCurrentUserId();
        Object result = null;
        //获取redis锁
        flag = distributedLocker.tryLock(lockKey, lock.timeUnit(), lock.waitTime(), lock.leaseTime());
        if (flag) {
            if (!Thread.currentThread().isInterrupted()) {
                result = point.proceed();
            }
        }
        if (flag) {
            distributedLocker.unlock(lockKey);
        }
        return result;
    }

}
