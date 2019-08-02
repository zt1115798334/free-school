package com.example.school.common.mysql.service;

import com.example.school.common.base.service.BaseService;
import com.example.school.common.mysql.entity.UserRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/09/25 11:36
 * description:
 */
public interface UserRegistrationService extends BaseService<UserRegistration, Long> {

    /**
     * 根据用户id查询所有的token值
     *
     * @param userId 用户id
     * @return list
     */
    List<String> findRegistrationIdByUserId(Long userId);
}
