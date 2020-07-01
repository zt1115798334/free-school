package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.UserPermissionRepository;
import com.example.school.common.mysql.service.UserPermission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
@AllArgsConstructor
@Service
public class UserPermissionImpl implements UserPermission {


    private final UserPermissionRepository userPermissionRepository;

    @Override
    public Iterable<com.example.school.common.mysql.entity.UserPermission> saveAll(Iterable<com.example.school.common.mysql.entity.UserPermission> t) {
        return userPermissionRepository.saveAll(t);
    }

    @Override
    public List<com.example.school.common.mysql.entity.UserPermission> findByUserId(Long userId) {
        return userPermissionRepository.findByUserId(userId);
    }
}
