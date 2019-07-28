package com.example.school.task.quartz.service.impl;

import com.example.school.common.utils.DateUtils;
import com.example.school.task.quartz.entity.QuartzEntity;
import com.example.school.task.quartz.service.JobService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/10 15:54
 * description:
 */
@AllArgsConstructor
@Slf4j
@Service
public class JobServiceImpl implements JobService {

    private final Scheduler scheduler;

    /**
     * 所有任务列表
     */
    public List<QuartzEntity> list() throws SchedulerException {
        List<QuartzEntity> list = Lists.newArrayList();
        for (String groupJob : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.groupEquals(groupJob))) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                    String cronExpression = "", createTime = "";

                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        cronExpression = cronTrigger.getCronExpression();
                        createTime = cronTrigger.getDescription();
                    }
                    QuartzEntity info = new QuartzEntity();
                    info.setId(1);
                    info.setJobName(jobKey.getName());
                    info.setJobGroup(jobKey.getGroup());
                    info.setJobDescription(jobDetail.getDescription());
                    info.setJobClassName(jobDetail.getJobClass().getName());
                    info.setJobStatus(triggerState.name());
                    info.setCronExpression(cronExpression);
                    info.setCreateTime(createTime);
                    list.add(info);
                }
            }
        }

        return list;
    }

    /**
     * 保存定时任务
     *
     * @param info info
     */
    @SuppressWarnings("unchecked")
    public void addJob(QuartzEntity info) throws SchedulerException, ClassNotFoundException {
        String jobName = info.getJobName(),
                jobGroup = info.getJobGroup(),
                jobClassName = info.getJobClassName(),
                cronExpression = info.getCronExpression(),
                jobDescription = info.getJobDescription(),
                createTime = DateUtils.formatDateTime(DateUtils.currentDateTime());

        if (checkExists(jobName, jobGroup)) {
            log.info("add job fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobName);
        }

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(schedBuilder).build();


        Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClassName);
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
        scheduler.scheduleJob(jobDetail, trigger);

    }

    /**
     * 修改定时任务
     *
     * @param info info
     */
    public void edit(QuartzEntity info) throws SchedulerException {
        String jobName = info.getJobName(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getCronExpression(),
                jobDescription = info.getJobDescription(),
                createTime = DateUtils.formatDateTime(DateUtils.currentDateTime());

        if (!checkExists(jobName, jobGroup)) {
            log.info("edit job fail, job is not exist, jobGroup:{}, jobName:{}", jobGroup, jobName);
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        JobKey jobKey = new JobKey(jobName, jobGroup);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(cronScheduleBuilder).build();

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        jobDetail.getJobBuilder().withDescription(jobDescription);
        HashSet<Trigger> triggerSet = new HashSet<>();
        triggerSet.add(cronTrigger);

        scheduler.scheduleJob(jobDetail, triggerSet, true);

    }

    /**
     * 删除定时任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     */
    public void delete(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        if (checkExists(jobName, jobGroup)) {
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            log.info("delete job, triggerKey:{},jobGroup:{}, jobName:{}", triggerKey, jobGroup, jobName);
        }

    }

    /**
     * 暂停定时任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     */
    public void pause(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.pauseTrigger(triggerKey);
            log.info("pause job success, triggerKey:{},jobGroup:{}, jobName:{}", triggerKey, jobGroup, jobName);
        }

    }

    /**
     * 重新开始任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     */
    public void resume(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.resumeTrigger(triggerKey);
            log.info("resume job success,triggerKey:{},jobGroup:{}, jobName:{}", triggerKey, jobGroup, jobName);
        }

    }

    /**
     * 验证是否存在
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @throws SchedulerException 自定义异常
     */
    public boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        return scheduler.checkExists(triggerKey);

    }
}
