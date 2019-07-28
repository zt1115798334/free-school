package com.example.school.shiro.base;

import com.example.school.common.mysql.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 17:26
 * description:
 */
public interface CurrentUser {

    default User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    default Long getCurrentUserId() {
        return getCurrentUser().getId();
    }


}
