package com.example.school.shiro.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 17:04
 * description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的名称。
     * 如果lockName可以确定，直接设置该属性。
     */
    String lockName() default "";

    /**
     * lockName前缀
     */
    String lockNamePre() default "locker";
    /**
     * 最长等待时间。
     * 该字段只有当tryLock()返回true才有效。
     */
    int waitTime() default 10;
    /**
     * 锁超时时间。
     * 超时时间过后，锁自动释放。
     * 建议：
     *   尽量缩简需要加锁的逻辑。
     */
    int leaseTime() default 5;
    /**
     * 时间单位。默认为秒。
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
