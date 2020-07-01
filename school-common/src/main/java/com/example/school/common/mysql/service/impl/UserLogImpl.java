package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.UserLogRepository;
import com.example.school.common.mysql.service.UserLog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserLogImpl implements UserLog {

    private final UserLogRepository userLogRepository;

    @Override
    public com.example.school.common.mysql.entity.UserLog save(com.example.school.common.mysql.entity.UserLog userLog) {
        return userLogRepository.save(userLog);
    }

    @Override
    public void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        userLogRepository.deleteByTimeBetween(startTime, endTime);
    }
}
