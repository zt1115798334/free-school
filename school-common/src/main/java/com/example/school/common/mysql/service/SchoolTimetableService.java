package com.example.school.common.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.mysql.entity.SchoolTimetable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolTimetableService extends BaseService<SchoolTimetable, Long> {


    JSONObject findSchoolTimetable(Long userId, String semester, Integer weeklyTimes);
}
