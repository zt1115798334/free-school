package com.example.school.task.task.handler;

import com.example.school.common.mysql.service.User;
import com.example.school.task.task.page.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/14 16:04
 * description:
 */
@Slf4j
public abstract class UserPageHandler extends PageHandler<com.example.school.common.mysql.entity.User> {

    @Autowired
    private User userService;

    protected Page<com.example.school.common.mysql.entity.User> getPageList(int pageNumber) {
        return userService.findPageByEntity(new com.example.school.common.mysql.entity.User());
    }

}
