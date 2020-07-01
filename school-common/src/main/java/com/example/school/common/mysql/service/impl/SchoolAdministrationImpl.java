package com.example.school.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.repo.SchoolAdministrationRepository;
import com.example.school.common.mysql.service.SchoolAdministration;
import com.example.school.common.mysql.service.SchoolTimetable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
@AllArgsConstructor
@Service
public class SchoolAdministrationImpl implements SchoolAdministration {

    private final SchoolAdministrationRepository schoolAdministrationRepository;

    private final SchoolTimetable schoolTimetableService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.READ_UNCOMMITTED)
    public com.example.school.common.mysql.entity.SchoolAdministration save(com.example.school.common.mysql.entity.SchoolAdministration schoolAdministration) {
        Long userId = schoolAdministration.getUserId();
        String studentId = schoolAdministration.getStudentId();
        Optional<com.example.school.common.mysql.entity.SchoolAdministration> administrationOptional = this.findOptByUserId(userId);
        long count = schoolTimetableService.count(studentId);
        if (administrationOptional.isPresent()) {
            com.example.school.common.mysql.entity.SchoolAdministration dbAdministration = administrationOptional.get();
            dbAdministration.setStudentId(studentId);
            dbAdministration.setStudentPwd(schoolAdministration.getStudentPwd());
            dbAdministration.setUpdatedTime(currentDateTime());
            if (count > 0) {
                schoolAdministration.setFreshState(SysConst.FreshState.PAST.getCode());
            } else {
                schoolAdministration.setFreshState(SysConst.FreshState.FRESH.getCode());
            }
            dbAdministration.setAbnormalState(SysConst.AbnormalState.NORMAL.getCode());
            dbAdministration.setUsableState(SysConst.UsableState.AVAILABLE.getCode());
            return schoolAdministrationRepository.save(dbAdministration);
        } else {
            if (count > 0) {
                schoolAdministration.setFreshState(SysConst.FreshState.PAST.getCode());
            } else {
                schoolAdministration.setFreshState(SysConst.FreshState.FRESH.getCode());
            }
            schoolAdministration.setAbnormalState(SysConst.AbnormalState.NORMAL.getCode());
            schoolAdministration.setUsableState(SysConst.UsableState.AVAILABLE.getCode());
            schoolAdministration.setCreatedTime(currentDateTime());
            return schoolAdministrationRepository.save(schoolAdministration);
        }
    }

    @Override
    public com.example.school.common.mysql.entity.SchoolAdministration saveSchoolAdministration(Long userId, Short schoolCode, String studentId, String studentPwd) {
//        String studentPwd = schoolAdministration.getStudentPwd();
//        String studentPwdRsa = RSAUtils.encryptBASE64(RSAUtils.encryptByPrivateKey(studentPwd.getBytes(), schoolProperties.getRsa().getPrivateKey()));
//        schoolAdministration.setStudentPwd(studentPwdRsa);
        com.example.school.common.mysql.entity.SchoolAdministration schoolAdministration = new com.example.school.common.mysql.entity.SchoolAdministration(userId, schoolCode, studentId, studentPwd);
        return this.save(schoolAdministration);
    }

    @Override
    public void deleteSchoolAdministration(Long userId) {
        schoolAdministrationRepository.deleteByUserId(userId);
    }

    @Override
    public void modifySchoolAdministrationUsableStateToNotAvailable(Long userId) {
        Optional<com.example.school.common.mysql.entity.SchoolAdministration> administrationOptional = this.findOptByUserId(userId);
        administrationOptional.ifPresent(schoolAdministration -> {
            schoolAdministration.setUsableState(SysConst.UsableState.NOT_AVAILABLE.getCode());
            schoolAdministrationRepository.save(schoolAdministration);
        });
    }

    @Override
    public Optional<com.example.school.common.mysql.entity.SchoolAdministration> findOptByUserId(Long userId) {
        return schoolAdministrationRepository.findByUserId(userId);
    }

    @Override
    public com.example.school.common.mysql.entity.SchoolAdministration findSchoolAdministration(Long userId) {
        return this.findOptByUserId(userId).orElseThrow(() -> new OperationException("你还没有绑定呢。。"));
    }


    @Override
    public JSONObject findSchoolTimetable(Long userId, String semester, Integer weeklyTimes) {
        com.example.school.common.mysql.entity.SchoolAdministration schoolAdministration = this.findSchoolAdministration(userId);
        List<com.example.school.common.mysql.entity.SchoolTimetable> timetableList = schoolTimetableService.findByStudentIdAndSemesterAndWeeklyTimes(schoolAdministration.getStudentId(), semester, weeklyTimes);
        Map<String, String> colorMap = SysConst.getColorBySchoolTimetable(timetableList);
       /* Map<String, Map<Short, SchoolTimetable>> timetableMap = timetableList.stream()
                .collect(groupingBy(SchoolTimetable::getWeek, toMap(SchoolTimetable::getClassTimes, Function.identity())));
        JSONObject weekJSON = new JSONObject();
        for (String week : SysConst.getAllWeekType()) {
            Map<Short, SchoolTimetable> weekMap = timetableMap.getOrDefault(week, Collections.emptyMap());
            JSONObject classTimeJSON = new JSONObject();
            for (Short classTimes : SysConst.getAllClassTimesCode()) {
                JSONObject other = new JSONObject();
                other.put("curriculum", "-");
                other.put("color", SysConst.Color.PANTONE_GCMI_91.getColorRGB());
                JSONObject curriculumJSON = Optional.ofNullable(weekMap.get(classTimes))
                        .map(schoolTimetable -> {
                            String curriculum = schoolTimetable.getCurriculum();
                            JSONObject json = new JSONObject();
                            json.put("curriculum", curriculum);
                            json.put("color", colorMap.get(curriculum));
                            return json;
                        })
                        .orElse(other);
                classTimeJSON.put(String.valueOf(classTimes), curriculumJSON);
            }
            weekJSON.put(week, classTimeJSON);
        }*/

        Map<String, Map<Short, List<com.example.school.common.mysql.entity.SchoolTimetable>>> timetableMap = timetableList.stream()
                .collect(groupingBy(com.example.school.common.mysql.entity.SchoolTimetable::getWeek, groupingBy(com.example.school.common.mysql.entity.SchoolTimetable::getClassTimes)));
        JSONObject weekJSON = new JSONObject();
        for (String week : SysConst.getAllWeekType()) {
            Map<Short, List<com.example.school.common.mysql.entity.SchoolTimetable>> weekMap = timetableMap.getOrDefault(week, Collections.emptyMap());
            JSONObject classTimeJSON = new JSONObject();
            for (Short classTimes : SysConst.getAllClassTimesCode()) {
                JSONObject other = new JSONObject();
                other.put("curriculum", "-");
                other.put("color", SysConst.Color.PANTONE_GCMI_91.getColorRGB());
                JSONObject curriculumJSON = Optional.ofNullable(weekMap.get(classTimes))
                        .map(schoolTimetable -> {
                            JSONObject json = new JSONObject();
                            if (schoolTimetable.size() != 0) {
                                String curriculum = schoolTimetable.get(0).getCurriculum();
                                json.put("curriculum", curriculum);
                                json.put("color", colorMap.get(curriculum));
                            } else {
                                json.put("curriculum", "-");
                                json.put("color", SysConst.Color.PANTONE_GCMI_91.getColorRGB());
                            }
                            return json;
                        })
                        .orElse(other);
                classTimeJSON.put(String.valueOf(classTimes), curriculumJSON);
            }
            weekJSON.put(week, classTimeJSON);
        }


        JSONObject result = new JSONObject();
        result.put("curriculum", weekJSON);
        return result;
    }
}
