package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.SchoolAdministration;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolAdministrationRepository extends CrudRepository<SchoolAdministration, Long> {

    Optional<SchoolAdministration> findByUserId(Long userId);
}
