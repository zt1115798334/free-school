package com.example.school.common.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.service.Base;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface Comment extends Base<com.example.school.common.mysql.entity.Comment, Long> {

    com.example.school.common.mysql.entity.Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId);

    void adoptComment(Long id, Long topicId, Short topicType);

    com.example.school.common.mysql.entity.Comment findComment(Long id);

    JSONObject countComment(Long topicId, Short topicType);

    Map<Long, Long> countComment(List<Long> topicId, Short topicType);

    PageImpl<RoCommentStatus> findRoCommentStatusPage(com.example.school.common.mysql.entity.Comment comment, Long userId);

    PageImpl<RoCommentStatus> findRoCommentAndReplyStatusPage(com.example.school.common.mysql.entity.Comment comment, Long userId);

}
