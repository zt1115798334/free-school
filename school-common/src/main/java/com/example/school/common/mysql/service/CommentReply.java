package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.service.Base;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentReply extends Base<com.example.school.common.mysql.entity.CommentReply, Long> {

    com.example.school.common.mysql.entity.CommentReply saveCommentReplyToComment(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId);

    com.example.school.common.mysql.entity.CommentReply saveCommentReplyToReply(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId);

    List<com.example.school.common.mysql.entity.CommentReply> findCommentReplyList(Long commentId);

    List<com.example.school.common.mysql.entity.CommentReply> findCommentReplyList(List<Long> commentIdList);

    List<RoCommentReplyStatus> findRoCommentReplyStatusList(Long commentId);

    List<RoCommentReplyStatus> findRoCommentReplyStatusList(List<Long> commentIdList);

}
