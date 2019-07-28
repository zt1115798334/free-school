package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Zan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 20:11
 * description:
 */
public interface ZanRepository extends CrudRepository<Zan, Long> {

    List<Zan> findByUserIdAndTopicIdInAndTopicTypeAndZanTypeAndZanState(Long userId, List<Long> topicId, Short topicType, Short zanType, Short zanState);

    Optional<Zan> findByUserIdAndTopicIdAndTopicTypeAndZanType(Long userId, Long topicId, Short topicType, Short zanType);

    List<Zan> findByTopicIdInAndTopicTypeAndZanTypeAndZanState(List<Long> topicId, Short topicType, Short zanType, Short zanState);
}
