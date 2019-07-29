package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.constant.SysConst;
import com.example.school.common.mysql.entity.CommentReply;
import com.example.school.common.mysql.repo.CommentReplyRepository;
import com.example.school.common.mysql.service.CommentReplyService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.tools.JPushTool;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.UserUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
@AllArgsConstructor
@Service
public class CommentReplyServiceImpl implements CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;

    private final UserService userService;

    private final JPushTool jPushTool;

    @Override
    public CommentReply save(CommentReply commentReply) {
        commentReply.setCreatedTime(currentDateTime());
        commentReply.setDeleteState(UN_DELETED);
        return commentReplyRepository.save(commentReply);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public CommentReply saveCommentReplyToComment(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId) {
//        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new CommentReply(commentId, SysConst.ReplyType.COMMENT.getCode(), replyId, content, toUserId, fromUserId));
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public CommentReply saveCommentReplyToReply(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId) {
//        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new CommentReply(commentId, SysConst.ReplyType.REPLY.getCode(), replyId, content, toUserId, fromUserId));
    }


    @Override
    public List<CommentReply> findCommentReplyList(Long commentId) {
        return commentReplyRepository.findByCommentIdAndDeleteState(commentId, UN_DELETED);
    }

    @Override
    public List<CommentReply> findCommentReplyList(List<Long> commentIdList) {
        return commentReplyRepository.findByCommentIdInAndDeleteState(commentIdList, UN_DELETED);
    }

    @Override
    public List<RoCommentReplyStatus> findRoCommentReplyStatusList(Long commentId) {
        List<CommentReply> commentReplyList = this.findCommentReplyList(commentId);
        return getRoCommentReplyStatuses(commentReplyList);
    }

    @Override
    public List<RoCommentReplyStatus> findRoCommentReplyStatusList(List<Long> commentIdList) {
        List<CommentReply> commentReplyList = this.findCommentReplyList(commentIdList);
        return getRoCommentReplyStatuses(commentReplyList);
    }

    private List<RoCommentReplyStatus> getRoCommentReplyStatuses(List<CommentReply> commentReplyList) {
        List<Long> userIdList = commentReplyList.parallelStream()
                .map(commentReply -> Lists.newArrayList(commentReply.getToUserId(), commentReply.getFromUserId()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, RoUser> roUserMap = userService.findMapRoUserByUserId(userIdList);
        return commentReplyList.stream().map(commentReply -> {
            Long toUserId = commentReply.getToUserId();
            Long fromUserId = commentReply.getFromUserId();
            return new RoCommentReplyStatus(
                    commentReply.getCommentId(), commentReply.getReplyType(), commentReply.getReplyId(),
                    commentReply.getContent(),
                    toUserId, roUserMap.getOrDefault(toUserId, UserUtils.getDefaultRoUser()),
                    fromUserId, roUserMap.getOrDefault(fromUserId, UserUtils.getDefaultRoUser()),
                    DateUtils.formatDateTime(commentReply.getCreatedTime()));
        }).collect(Collectors.toList());
    }

}
