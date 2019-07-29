package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
public interface CollectionRepository extends CrudRepository<Collection, Long>,
        JpaSpecificationExecutor<Collection> {

    Optional<Collection> findByUserIdAndTopicIdAndTopicType(Long userId, Long topicId, Short topicType);

    List<Collection> findByUserIdAndTopicIdInAndTopicTypeAndCollectionState(Long userId, List<Long> topicId, Short topicType, Short collectionState);

}
