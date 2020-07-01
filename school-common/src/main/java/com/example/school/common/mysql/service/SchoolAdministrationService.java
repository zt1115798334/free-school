package com.example.school.common.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.SchoolAdministration;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolAdministrationService extends Base<SchoolAdministration, Long> {

    SchoolAdministration saveSchoolAdministration(Long userId, Short schoolCode, String studentId, String studentPwd);

    void deleteSchoolAdministration(Long userId);

    void modifySchoolAdministrationUsableStateToNotAvailable(Long userId);

    Optional<SchoolAdministration> findOptByUserId(Long userId);

    SchoolAdministration findSchoolAdministration(Long userId);

    JSONObject findSchoolTimetable(Long userId, String semester, Integer weeklyTimes);


}
