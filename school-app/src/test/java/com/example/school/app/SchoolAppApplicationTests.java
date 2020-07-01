package com.example.school.app;

import com.example.school.common.mysql.service.UserRegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolAppApplicationTests {
    @Autowired
    private UserRegistrationService userRegistrationService;


    @Test
    public void contextLoads() {
        com.example.school.common.mysql.entity.UserRegistration userRegistration = new com.example.school.common.mysql.entity.UserRegistration(1L, "1507bfd3f7be5dec45e", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNTEzMDA5NzU4MiIsInVzZXJfaWQiOjEsIm5vbl9leHBpcmVkIjpmYWxzZSwiZXhwIjoxNTY3MzI5NzgzLCJpYXQiOjE1NjQ3Mzc3ODMsIm5vbl9sb2NrZWQiOmZhbHNlLCJqdGkiOiIyYWE4ZDJmMDkwNzQ0ZTA2OWE2Yjc0NTcxNGY2MWY1YyJ9.vZr6A0EcnFqRCwH0ldhp7X3f0y-IdNtrXChdh33f-ek");
        Thread thread1 = new Thread(() -> {
            userRegistrationService.save(userRegistration);
        });
        Thread thread2 = new Thread(() -> {
            userRegistrationService.save(userRegistration);
        });
        thread1.start();
        thread2.start();
    }

}
