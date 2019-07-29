package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.SignRecord;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
public interface SignRecordRepository extends CrudRepository<SignRecord, Long> {

    Optional<SignRecord> findByUserIdAndDateMonth(Long userId, LocalDate dateMonth);
}
