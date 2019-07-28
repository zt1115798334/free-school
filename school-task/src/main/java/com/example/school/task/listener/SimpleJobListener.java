package com.example.school.task.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/15 10:16
 * description:
 */
@Slf4j
public class SimpleJobListener implements JobListener {

    private String methodName;      // 方法名
    private long startTime;         // 开始时间

    /**
     * 用于获取该JobListener的名称
     *
     * @return
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Scheduler在JobDetail将要被执行时调用这个方法。
     *
     * @param context
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        methodName=context.getJobDetail().getKey().getName();
        log.info("开始执行 " +  "，方法名称为" + methodName);
        startTime = System.currentTimeMillis();
    }

    /**
     * Scheduler在JobDetail即将被执行，但又被TriggerListerner否决时会调用该方法
     *
     * @param context
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    /**
     * Scheduler在JobDetail被执行之后调用这个方法
     *
     * @param context
     * @param jobException
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        long E_time = System.currentTimeMillis() - startTime;
        log.info("执行 " + methodName + " 耗时为：" + E_time + "ms");
    }
}
