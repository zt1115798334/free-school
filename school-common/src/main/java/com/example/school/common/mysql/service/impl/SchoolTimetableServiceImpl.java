package com.example.school.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.ro.RoCurriculum;
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

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
        Map<String, Map<Short, SchoolTimetable>> timetableMap = timetableList.stream()
                .collect(groupingBy(SchoolTimetable::getWeek, toMap(SchoolTimetable::getClassTimes, Function.identity())));
        JSONObject weekJSON = new JSONObject();
        for (String week : SysConst.getAllWeekType()) {
            Map<Short, SchoolTimetable> weekMap = timetableMap.getOrDefault(week, Collections.emptyMap());
            JSONObject classTimeJSON = new JSONObject();
            for (Short classTimes : SysConst.getAllClassTimesCode()) {
                RoCurriculum roCurriculum = Optional.ofNullable(weekMap.get(classTimes))
                        .map(schoolTimetable -> {

                            return new RoCurriculum(schoolTimetable.getCurriculum(),
                                    schoolTimetable.getWeeklyTimesCurr(),
                                    schoolTimetable.getClassTimesCurr(),
                                    schoolTimetable.getTeacherCurr(),
                                    schoolTimetable.getClassroomCurr());
                        })
                        .orElse(new RoCurriculum());
                classTimeJSON.put(String.valueOf(classTimes), roCurriculum);
            }
            weekJSON.put(week, classTimeJSON);
        }


        JSONObject result = new JSONObject();
        result.put("curriculum", weekJSON);
        return result;
    }

}
