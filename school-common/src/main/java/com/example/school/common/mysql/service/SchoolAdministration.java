package com.example.school.common.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.service.Base;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolAdministration extends Base<com.example.school.common.mysql.entity.SchoolAdministration, Long> {

    com.example.school.common.mysql.entity.SchoolAdministration saveSchoolAdministration(Long userId, Short schoolCode, String studentId, String studentPwd);

    void deleteSchoolAdministration(Long userId);

    void modifySchoolAdministrationUsableStateToNotAvailable(Long userId);

    Optional<com.example.school.common.mysql.entity.SchoolAdministration> findOptByUserId(Long userId);

    com.example.school.common.mysql.entity.SchoolAdministration findSchoolAdministration(Long userId);

    JSONObject findSchoolTimetable(Long userId, String semester, Integer weeklyTimes);


}
