package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentRepository extends CrudRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {

    List<Comment> findByTopicIdInAndTopicTypeAndDeleteState(List<Long> topicId, Short topicType, Short deleteState);
}
