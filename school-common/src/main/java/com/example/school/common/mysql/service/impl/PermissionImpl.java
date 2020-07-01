package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.PermissionRepository;
import com.example.school.common.mysql.service.Permission;
import com.example.school.common.mysql.service.UserPermission;
import com.example.school.common.utils.DateUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.school.common.constant.SysConst.AccountType;
import static com.example.school.common.constant.SysConst.PermissionType;
import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
@AllArgsConstructor
@Service
public class PermissionImpl implements Permission {


    private final PermissionRepository permissionRepository;

    private final UserPermission userPermissionService;

    @Override
    public List<com.example.school.common.mysql.entity.Permission> findAll() {
        return (List<com.example.school.common.mysql.entity.Permission>) permissionRepository.findAll();
    }

    @Override
    public List<com.example.school.common.mysql.entity.Permission> findByIds(List<Long> id) {
        return (List<com.example.school.common.mysql.entity.Permission>) permissionRepository.findAllById(id);
    }

    @Override
    public void saveSysSystemPermission(Long userId, String accountType) {
        List<com.example.school.common.mysql.entity.Permission> permissionList = Lists.newLinkedList();
        if (Objects.equal(accountType, AccountType.ADMIN.getType())) {
            permissionList = this.findAll();
        }
        if (Objects.equal(accountType, AccountType.STUDENT_PRESIDENT.getType())) {
            permissionList = this.findStudentPresidentPermission();
        }
        List<com.example.school.common.mysql.entity.UserPermission> userPermissionList = permissionList.stream()
                .map(permission -> new com.example.school.common.mysql.entity.UserPermission(userId, permission.getId(), DateUtils.currentDateTime()))
                .collect(toList());
        userPermissionService.saveAll(userPermissionList);
    }

    @Override
    public List<com.example.school.common.mysql.entity.Permission> findAdminPermission() {
        return this.findAll();
    }

    @Override
    public List<com.example.school.common.mysql.entity.Permission> findStudentPresidentPermission() {
        return permissionRepository.findByPermissionType(PermissionType.STUDENT_PRESIDENT.getType());
    }

    @Override
    public List<com.example.school.common.mysql.entity.Permission> findByUserId(Long userId) {
        List<Long> permissionIdList = userPermissionService.findByUserId(userId).stream()
                .map(com.example.school.common.mysql.entity.UserPermission::getPermissionId)
                .collect(toList());
        return this.findByIds(permissionIdList);
    }
}
