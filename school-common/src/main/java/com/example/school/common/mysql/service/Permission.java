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
public interface Permission extends Base<com.example.school.common.mysql.entity.Permission, Long> {

    void saveSysSystemPermission(Long userId, String accountType);

    List<com.example.school.common.mysql.entity.Permission> findAdminPermission();

    List<com.example.school.common.mysql.entity.Permission> findStudentPresidentPermission();

    List<com.example.school.common.mysql.entity.Permission> findByUserId(Long userId);

}
