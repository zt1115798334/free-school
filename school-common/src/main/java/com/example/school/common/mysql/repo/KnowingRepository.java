package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Knowing;
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
 * date: 2019/07/15 11:34
 * description:
 */
public interface KnowingRepository extends CrudRepository<Knowing, Long>,
        JpaSpecificationExecutor<Knowing> {
    Optional<Knowing> findByIdAndDeleteState(Long id, Short deleteState);

    List<Knowing> findByIdInAndDeleteState(List<Long> id, Short deleteState);

    @Modifying
    @Query(value = "update Knowing set state =:afterState where userId in :userId and state = :beforeState and deleteState =:deleteState ")
    void updateState(List<Long> userId, String beforeState, String afterState, Short deleteState);

    @Modifying
    @Query(value = "update Knowing set browsingVolume = browsingVolume + 1 where id =:id")
    void incrementBrowsingVolume(Long id);
}
