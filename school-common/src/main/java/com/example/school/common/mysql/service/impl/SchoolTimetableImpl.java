package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.SchoolTimetableRepository;
import com.example.school.common.mysql.service.SchoolTimetable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
public class SchoolTimetableImpl implements SchoolTimetable {


    private final SchoolTimetableRepository schoolTimetableRepository;

    @Override
    public long count(String studentId) {
        return schoolTimetableRepository.countByStudentId(studentId);
    }

    @Override
    public List<com.example.school.common.mysql.entity.SchoolTimetable> findByStudentIdAndSemesterAndWeeklyTimes(String studentId, String semester, Integer weeklyTimes) {
        return schoolTimetableRepository.findByStudentIdAndSemesterAndWeeklyTimes(studentId, semester, weeklyTimes);
    }


}
