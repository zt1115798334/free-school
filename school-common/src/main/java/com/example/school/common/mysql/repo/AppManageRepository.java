package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.AppManage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/09 10:21
 * description:
 */
public interface AppManageRepository extends CrudRepository<AppManage, Long> {

    Optional<AppManage> findBySystemTypeAndDeleteState(String systemType, Short deleteState);
}
