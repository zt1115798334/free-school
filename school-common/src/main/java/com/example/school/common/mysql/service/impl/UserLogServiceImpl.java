package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.entity.UserLog;
import com.example.school.common.mysql.repo.UserLogRepository;
import com.example.school.common.mysql.service.UserLogService;
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
public class UserLogServiceImpl implements UserLogService {

    private final UserLogRepository userLogRepository;

    @Override
    public UserLog save(UserLog userLog) {
        return userLogRepository.save(userLog);
    }

    @Override
    public void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        userLogRepository.deleteByTimeBetween(startTime, endTime);
    }
}
