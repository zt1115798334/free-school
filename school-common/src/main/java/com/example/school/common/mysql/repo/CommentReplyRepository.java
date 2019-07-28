package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.CommentReply;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentReplyRepository extends CrudRepository<CommentReply,Long> {

    List<CommentReply> findByCommentIdAndDeleteState(Long commentId, Short deleteState);

    List<CommentReply> findByCommentIdInAndDeleteState(List<Long> commentId, Short deleteState);
}
