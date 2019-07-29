package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.UserPermission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
public interface UserPermissionRepository extends CrudRepository<UserPermission,Long> {

    List<UserPermission> findByUserId(Long userId);
}
