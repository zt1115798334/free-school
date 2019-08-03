package com.example.school.common.mysql.service;

import com.example.school.common.base.service.BaseService;
import com.example.school.common.mysql.entity.SchoolAdministration;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolAdministrationService extends BaseService<SchoolAdministration, Long> {

    SchoolAdministration saveSchoolAdministration(SchoolAdministration schoolAdministration);

    void modifySchoolAdministrationUsableStateToNotAvailable(Long userId);

    Optional<SchoolAdministration> findOptByUserId(Long userId);

    SchoolAdministration findSchoolAdministration(Long userId);

}