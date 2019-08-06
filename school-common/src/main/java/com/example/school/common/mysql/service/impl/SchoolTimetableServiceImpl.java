package com.example.school.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.constant.SysConst;
import com.example.school.common.mysql.entity.SchoolAdministration;
import com.example.school.common.mysql.entity.SchoolTimetable;
import com.example.school.common.mysql.repo.SchoolTimetableRepository;
import com.example.school.common.mysql.service.SchoolAdministrationService;
import com.example.school.common.mysql.service.SchoolTimetableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class SchoolTimetableServiceImpl implements SchoolTimetableService {


    private final SchoolTimetableRepository schoolTimetableRepository;

    private final SchoolAdministrationService schoolAdministrationService;

    @Override
    public JSONObject findSchoolTimetable(Long userId, String semester, Integer weeklyTimes) {
        SchoolAdministration schoolAdministration = schoolAdministrationService.findSchoolAdministration(userId);
        List<SchoolTimetable> timetableList = schoolTimetableRepository.findByStudentIdAndSemesterAndWeeklyTimes(schoolAdministration.getStudentId(), semester, weeklyTimes);
//        Map<Short, Map<String, SchoolTimetable>> timetableMap = timetableList.stream()
//                .collect(groupingBy(SchoolTimetable::getClassTimes, toMap(SchoolTimetable::getWeek, Function.identity())));
//        List<List<String>> curriculumList = SysConst.getAllClassTimesCode().stream()
//                .map(classTimes -> {
//                    Map<String, SchoolTimetable> weekMap = timetableMap.getOrDefault(classTimes, Collections.emptyMap());
//                    return SysConst.getAllWeekType().stream()
//                            .map(week -> Optional.ofNullable(weekMap.get(week))
//                                    .map(SchoolTimetable::getCurriculum)
//                                    .orElse("-")).collect(toList());
//                }).collect(toList());
        Map<String, Map<Short, SchoolTimetable>> timetableMap = timetableList.stream()
                .collect(groupingBy(SchoolTimetable::getWeek, toMap(SchoolTimetable::getClassTimes, Function.identity())));
        List<List<String>> curriculumList = SysConst.getAllWeekType().stream()
                .map(week -> {
                    Map<Short, SchoolTimetable> weekMap = timetableMap.getOrDefault(week, Collections.emptyMap());
                    return SysConst.getAllClassTimesCode().stream()
                            .map(classTimes -> Optional.ofNullable(weekMap.get(classTimes))
                                    .map(SchoolTimetable::getCurriculum)
                                    .orElse("-")).collect(toList());
                }).collect(toList());


        JSONObject result = new JSONObject();
        result.put("curriculumList", curriculumList);
        return result;
    }

}
