package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Permission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
public interface PermissionRepository extends CrudRepository<Permission,Long> {

    List<Permission> findByPermissionType(String permissionType);
}
