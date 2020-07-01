package com.example.school;

import com.example.school.task.task.handler.BaseUserPageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolTaskApplicationTests {

    @Resource(name = "modifySateToAfterReleaseHandler")
    private BaseUserPageHandler modifySateToAfterReleaseHandler;

    @Test
    public void contextLoads() {
        modifySateToAfterReleaseHandler.handle();
    }

    @Autowired
    private Scheduler scheduler;
    @Test
    public void clear() throws SchedulerException {
        scheduler.clear();

    }
}
