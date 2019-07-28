package com.example.school;

import com.example.school.task.task.handler.UserPageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolTaskApplicationTests {

    @Resource(name = "modifySateToAfterReleaseHandler")
    private UserPageHandler modifySateToAfterReleaseHandler;

    @Test
    public void contextLoads() {
        modifySateToAfterReleaseHandler.handle();

    }

}
