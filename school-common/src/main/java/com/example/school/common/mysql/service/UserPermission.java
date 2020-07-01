package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
public interface UserPermission extends Base<com.example.school.common.mysql.entity.UserPermission, Long> {

    List<com.example.school.common.mysql.entity.UserPermission> findByUserId(Long userId);

}
