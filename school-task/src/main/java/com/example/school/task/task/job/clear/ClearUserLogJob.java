package com.example.school.task.task.job.clear;

import com.example.school.common.mysql.service.UserLogService;
import com.example.school.common.mysql.service.VerificationCodeLogService;
import com.example.school.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/11 9:35
 * description: 删除六个月前用户日志定时器
 */
@Slf4j
@Component
public class ClearUserLogJob implements Job {

    @Resource
    private UserLogService userLogService;

    @Resource
    private VerificationCodeLogService verificationCodeLogService;


    /**
     * 删除三个月前日志定时器
     *
     * @param jobExecutionContext jobExecutionContext
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LocalDateTime beforeMarch = DateUtils.currentDateTimeAddMonth(-6);
        LocalDateTime startTime = DateUtils.dateTimeToMonthFirstDay(beforeMarch);
        LocalDateTime endTime = DateUtils.dateTimeToMonthLastDay(beforeMarch);
        userLogService.deleteByTimeBetween(startTime, endTime);
        verificationCodeLogService.deleteByTimeBetween(startTime, endTime);
    }
}
