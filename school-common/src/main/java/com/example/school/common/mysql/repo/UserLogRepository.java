package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.UserLog;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
public interface UserLogRepository extends CrudRepository<UserLog, Long> {

    void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
