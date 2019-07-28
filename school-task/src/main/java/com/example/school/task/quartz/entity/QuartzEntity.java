package com.example.school.task.quartz.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/10 15:55
 * description:
 */
@Getter
@Setter
@NoArgsConstructor
public class QuartzEntity implements Serializable {
    private static final long serialVersionUID = -8054692082716173379L;

    private int id = 0;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务描述
     */
    private String jobDescription;

    /**
     * 执行类
     */
    private String jobClassName;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 任务表达式
     */
    private String cronExpression;

    private String createTime;

    public QuartzEntity(String jobName, String jobGroup, String jobDescription, String jobClassName, String cronExpression) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobDescription = jobDescription;
        this.jobClassName = jobClassName;
        this.cronExpression = cronExpression;
    }
}
