package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Information;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 15:11
 * description:
 */
public interface InformationRepository extends CrudRepository<Information, Long>,
        JpaSpecificationExecutor<Information> {
    Optional<Information> findByIdAndDeleteState(Long id, Short deleteState);

    List<Information> findByIdInAndDeleteState(List<Long> id, Short deleteState);

    @Modifying
    @Query(value = "update Information set state =:afterState where userId in :userId and state = :beforeState and deleteState =:deleteState ")
    void updateState(List<Long> userId, String beforeState, String afterState, Short deleteState);

    @Modifying
    @Query(value = "update Information set browsingVolume = browsingVolume + 1 where id =:id")
    @Transactional(rollbackFor = RuntimeException.class)
    void incrementBrowsingVolume(Long id);
}
