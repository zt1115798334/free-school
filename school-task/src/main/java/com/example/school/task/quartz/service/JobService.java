package com.example.school.task.quartz.service;

import com.example.school.task.quartz.entity.QuartzEntity;
import org.quartz.SchedulerException;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/10 15:54
 * description:
 */
public interface JobService {

    List<QuartzEntity> list() throws SchedulerException;

    void addJob(QuartzEntity info) throws SchedulerException, ClassNotFoundException;

    void edit(QuartzEntity info) throws SchedulerException;

    void delete(String jobName, String jobGroup) throws SchedulerException;

    void pause(String jobName, String jobGroup) throws SchedulerException;

    void resume(String jobName, String jobGroup) throws SchedulerException;

    boolean checkExists(String jobName, String jobGroup) throws SchedulerException;
}
