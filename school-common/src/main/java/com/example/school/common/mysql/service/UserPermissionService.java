package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.UserPermission;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
public interface UserPermissionService extends Base<UserPermission, Long> {

    List<UserPermission> findByUserId(Long userId);

}
