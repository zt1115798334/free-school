package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.mysql.entity.Comment;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentService extends BaseService<Comment, Long> {

    Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId);

    Map<Long, Long> countComment(List<Long> topicId, Short topicType);

    CustomPage<RoCommentStatus> findRoCommentStatusPage(Comment comment, Long userId);

    CustomPage<RoCommentStatus> findRoCommentAndReplyStatusPage(Comment comment, Long userId);

}
