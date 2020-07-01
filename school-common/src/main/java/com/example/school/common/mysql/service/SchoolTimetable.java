package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolTimetable extends Base<com.example.school.common.mysql.entity.SchoolTimetable, Long> {


    long count(String studentId);

    List<com.example.school.common.mysql.entity.SchoolTimetable> findByStudentIdAndSemesterAndWeeklyTimes(String studentId, String semester, Integer weeklyTimes);

}
