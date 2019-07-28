package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.RecordTime;
import com.example.school.common.mysql.entity.Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 21:52
 * description:
 */
public interface RecordTimeRepository extends CrudRepository<RecordTime, Long>,
        JpaSpecificationExecutor<RecordTime> {
    Optional<RecordTime> findByIdAndDeleteState(Long id, Short deleteState);

    List<RecordTime> findByUserIdInAndStateAndDeleteState(List<Long> userId, String state, Short deleteState);

    @Modifying
    @Query(value = "update RecordTime set state =:afterState where userId in :userId and state = :beforeState and deleteState =:deleteState ")
    void updateState(List<Long> userId, String beforeState, String afterState, Short deleteState);

}
