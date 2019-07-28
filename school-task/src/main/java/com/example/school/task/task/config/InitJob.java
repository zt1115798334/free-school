package com.example.school.task.task.config;

import com.example.school.task.quartz.entity.QuartzEntity;
import com.example.school.task.quartz.service.JobService;
import com.example.school.task.task.job.clear.ClearUserLogJob;
import com.example.school.task.task.job.clear.ModifySateToAfterReleaseJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/25 11:31
 * description: 初始化 job
 */
@Slf4j
@AllArgsConstructor
@Component
public class InitJob implements ApplicationRunner {

    private final JobService jobService;


    private static final String zeroPerDay = "0 0 0 * * ?";    //每天零点
    private static final String zeroPerMonday = "0 0 0 ? * MON";    //每周一零点
    private static final String zeroPerMonth = "0 0 0 1 * ?";    //每月一号零点
    private static final String tenMinutesPerHour = "0 10 0/1 * * ?";    //每小时10分钟
    private static final String perHour = "0 0 0/1 * * ?";    //每小时
    private static final String oneHourPerMonth = "0 0 0/1 1 * ? ";    //每月一号一点

    private static final String perFiveMinutes = "0 0/5 * * * ?";    //每5分钟
    private static final String perThirtyMinutes = "0 0/30 * * * ?";    //每30分钟

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<QuartzEntity> quartzEntityList = jobService.list();
        if (quartzEntityList.size() == 0) {
            log.info("初始化任务...");
            QuartzEntity clearUserLogJob = new QuartzEntity(
                    "删除用户日志定时器",
                    "group1",
                    "删除用户日志定时器",
                    ClearUserLogJob.class.getCanonicalName(),
                    zeroPerMonth);
            QuartzEntity modifySateToAfterReleaseJob = new QuartzEntity(
                    "更新发表状态定时器",
                    "group1",
                    "更新发表状态定时器",
                    ModifySateToAfterReleaseJob.class.getCanonicalName(),
                    zeroPerDay);
            jobService.addJob(clearUserLogJob);
            jobService.addJob(modifySateToAfterReleaseJob);
            log.info("初始化完成...");
        }

    }
}
