package com.example.school.task.quartz.controller;

import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.web.AbstractController;
import com.example.school.task.quartz.entity.QuartzEntity;
import com.example.school.task.quartz.service.JobService;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/10 16:23
 * description:
 */
@AllArgsConstructor
@Controller
@RequestMapping("/job/")
public class JobController extends AbstractController {

    private final JobService jobService;

    /**
     * index.html
     */
    @GetMapping(value = {"", "/"})
    public String index() {
        return "index";
    }

    /**
     * add.html
     */
    @GetMapping(value = "add")
    public String add() {
        return "add";
    }

    /**
     * cron.html
     */
    @GetMapping(value = "cron")
    public String cron() {
        return "cron";
    }

    /**
     * 任务列表
     *
     * @return ResultMessage
     */
    @PostMapping(value = "list")
    @ResponseBody
    public ResultMessage list() throws SchedulerException {
        List<QuartzEntity> infos = jobService.list();
        int size = infos.size();
        return success(1, size, size, infos);
    }

    /**
     * 保存定时任务
     *
     * @param info info
     * @return ResultMessage
     */
    @PostMapping(value = "save")
    @ResponseBody
    public ResultMessage save(QuartzEntity info) throws ClassNotFoundException, SchedulerException {

        if (info.getId() == 0) {
            jobService.addJob(info);
        } else {
            jobService.edit(info);
        }
        return success();
    }

    /**
     * 删除定时任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return ResultMessage
     */
    @ResponseBody
    @PostMapping(value = "delete", produces = "application/json; charset=UTF-8")
    public ResultMessage delete(@RequestParam String jobName, @RequestParam String jobGroup) throws SchedulerException {
        jobService.delete(jobName, jobGroup);
        return success();
    }

    /**
     * 暂停定时任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return ResultMessage
     */
    @ResponseBody
    @PostMapping(value = "pause", produces = "application/json; charset=UTF-8")
    public ResultMessage pause(@RequestParam String jobName, @RequestParam String jobGroup) throws SchedulerException {

        jobService.pause(jobName, jobGroup);
        return success();
    }

    /**
     * 重新开始定时任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     * @return ResultMessage
     */
    @ResponseBody
    @PostMapping(value = "resume", produces = "application/json; charset=UTF-8")
    public ResultMessage resume(@RequestParam String jobName, @RequestParam String jobGroup) throws SchedulerException {

        jobService.resume(jobName, jobGroup);

        return success();
    }
}
