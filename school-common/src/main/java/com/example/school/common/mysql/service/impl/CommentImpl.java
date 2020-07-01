package com.example.school.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoTopicCommentMap;
import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.repo.CommentRepository;
import com.example.school.common.mysql.service.CommentReply;
import com.example.school.common.mysql.service.Comment;
import com.example.school.common.mysql.service.User;
import com.example.school.common.mysql.service.Zan;
import com.example.school.common.tools.JPushTool;
import com.example.school.common.utils.change.RoChangeEntityUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
@AllArgsConstructor
@Service
public class CommentImpl implements Comment {

    private final CommentRepository commentRepository;

    private final CommentReply commentReplyService;

    private final User userService;

    private final Zan zanService;

    private final JPushTool jPushTool;

    @Override
    public com.example.school.common.mysql.entity.Comment save(com.example.school.common.mysql.entity.Comment comment) {
        comment.setCreatedTime(currentDateTime());
        comment.setDeleteState(UN_DELETED);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long aLong) {
        commentRepository.findById(aLong).ifPresent(comment -> {
            comment.setDeleteState(DELETED);
            commentRepository.save(comment);
        });
    }

    @Override
    public Optional<com.example.school.common.mysql.entity.Comment> findByIdNotDelete(Long aLong) {
        return commentRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public Page<com.example.school.common.mysql.entity.Comment> findPageByEntity(com.example.school.common.mysql.entity.Comment comment) {
        List<SearchFilter> filters = getCommentFilter(comment);
        Specification<com.example.school.common.mysql.entity.Comment> specification = SearchFilter.bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(comment);
        return commentRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public com.example.school.common.mysql.entity.Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId) {
        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new com.example.school.common.mysql.entity.Comment(toUserId, topicId, topicType, content));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void adoptComment(Long id, Long topicId, Short topicType) {
        com.example.school.common.mysql.entity.Comment comment = this.findComment(id);
        if (Objects.equal(comment.getState(), SysConst.CommentState.ADOPT.getType())) {
            throw new OperationException("已经被采纳了");
        }
        List<com.example.school.common.mysql.entity.Comment> commentList = commentRepository.findByTopicIdAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        List<com.example.school.common.mysql.entity.Comment> dbComment = commentList.parallelStream()
                .peek(ct -> {
                    if (Objects.equal(ct.getId(), id)) {
                        ct.setState(SysConst.CommentState.ADOPT.getType());
                    } else {
                        ct.setState(SysConst.CommentState.NOT_ADOPTED.getType());
                    }
                })
                .collect(toList());
        commentRepository.saveAll(dbComment);
    }

    @Override
    public com.example.school.common.mysql.entity.Comment findComment(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public JSONObject countComment(Long topicId, Short topicType) {
        long count = commentRepository.countByTopicIdAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        JSONObject result = new JSONObject();
        result.put("commentNum", count);
        return result;
    }

    @Override
    public Map<Long, Long> countComment(List<Long> topicId, Short topicType) {
        List<com.example.school.common.mysql.entity.Comment> commentList = commentRepository.findByTopicIdInAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        return commentList.stream().collect(groupingBy(com.example.school.common.mysql.entity.Comment::getTopicId, counting()));
    }

    @Override
    public PageImpl<RoCommentStatus> findRoCommentStatusPage(com.example.school.common.mysql.entity.Comment comment, Long userId) {
        Page<com.example.school.common.mysql.entity.Comment> commentPage = this.findPageByEntity(comment);
        return this.resultRoCommentStatus(commentPage, comment.getTopicType(), userId, Collections.emptyList());
    }

    @Override
    public PageImpl<RoCommentStatus> findRoCommentAndReplyStatusPage(com.example.school.common.mysql.entity.Comment comment, Long userId) {
        Page<com.example.school.common.mysql.entity.Comment> commentPage = this.findPageByEntity(comment);
        List<Long> commentIdList = commentPage.stream().map(com.example.school.common.mysql.entity.Comment::getId).collect(toList());
        List<RoCommentReplyStatus> roCommentReplyStatusList = commentReplyService.findRoCommentReplyStatusList(commentIdList);
        return this.resultRoCommentStatus(commentPage, comment.getTopicType(), userId, roCommentReplyStatusList);

    }

    private RoTopicCommentMap getTopicCommentMethod(List<Long> userIdList, Long userId, List<Long> topicId, Short topicTyp) {
        Map<Long, RoUser> roUserMap = userService.findMapRoUserByUserId(userIdList);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, ZAN_COMMENT);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, ZAN_COMMENT);
        return new RoTopicCommentMap(roUserMap, zanNumMap, zanStateMap);
    }

    private PageImpl<RoCommentStatus> resultRoCommentStatus(Page<com.example.school.common.mysql.entity.Comment> page, Short topicTyp, Long userId, List<RoCommentReplyStatus> roCommentReplyStatusList) {
        List<Long> topicId = page.stream().map(com.example.school.common.mysql.entity.Comment::getId).collect(toList());
        List<Long> userIdList = page.stream().map(com.example.school.common.mysql.entity.Comment::getUserId).distinct().collect(toList());
        RoTopicCommentMap topicCommentMap = getTopicCommentMethod(userIdList, userId, topicId, topicTyp);
        Map<Long, Long> commentReplyNumMap = roCommentReplyStatusList.stream()
                .collect(groupingBy(RoCommentReplyStatus::getCommentId, counting()));
        Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap = roCommentReplyStatusList.stream()
                .collect(groupingBy(RoCommentReplyStatus::getCommentId));
        List<RoCommentStatus> roTransactionList = RoChangeEntityUtils
                .resultRoCommentStatus(page.getContent(), userId, topicCommentMap, commentReplyNumMap, roCommentReplyStatusMap);
        return new PageImpl<>(roTransactionList, page.getPageable(), page.getTotalElements());
    }


    private List<SearchFilter> getCommentFilter(com.example.school.common.mysql.entity.Comment comment) {
        List<SearchFilter> filters = Lists.newArrayList();
        filters.add(new SearchFilter("topicId", comment.getTopicId(), SearchFilter.Operator.EQ));
        filters.add(new SearchFilter("topicType", comment.getTopicType(), SearchFilter.Operator.EQ));
        filters.add(new SearchFilter("deleteState", UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }

}
