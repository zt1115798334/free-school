package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.SchoolTimetable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolTimetableService extends Base<SchoolTimetable, Long> {


    long count(String studentId);

    List<SchoolTimetable> findByStudentIdAndSemesterAndWeeklyTimes(String studentId, String semester, Integer weeklyTimes);

}
