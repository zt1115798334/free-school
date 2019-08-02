package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentRepository extends CrudRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {

    Optional<Comment> findByIdAndDeleteState(Long aLong, Short deleteState);

    List<Comment> findByTopicIdAndTopicTypeAndDeleteState(Long topicId, Short topicType, Short deleteState);

    long countByTopicIdAndTopicTypeAndDeleteState(Long topicId, Short topicType, Short deleteState);

    List<Comment> findByTopicIdInAndTopicTypeAndDeleteState(List<Long> topicId, Short topicType, Short deleteState);
}
