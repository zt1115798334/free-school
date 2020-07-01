package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.service.VerificationCodeLog;
import com.example.school.common.mysql.repo.VerificationCodeLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class VerificationCodeLogImpl implements VerificationCodeLog {

    private final VerificationCodeLogRepository verificationCodeLogRepository;

    @Override
    public com.example.school.common.mysql.entity.VerificationCodeLog save(com.example.school.common.mysql.entity.VerificationCodeLog verificationCodeLog) {
        return verificationCodeLogRepository.save(verificationCodeLog);
    }

    @Override
    public void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        verificationCodeLogRepository.deleteByCreatedTimeBetween(startTime, endTime);
    }
}
