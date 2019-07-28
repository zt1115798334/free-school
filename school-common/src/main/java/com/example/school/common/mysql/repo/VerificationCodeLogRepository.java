package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.VerificationCodeLog;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
public interface VerificationCodeLogRepository extends CrudRepository<VerificationCodeLog, Long> {

    void deleteByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

}
