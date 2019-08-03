package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.SchoolTimetable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
public interface SchoolTimetableRepository extends CrudRepository<SchoolTimetable, Long> {
}
