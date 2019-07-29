package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoTopicCommentMap;
import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.repo.CommentRepository;
import com.example.school.common.mysql.service.CommentReplyService;
import com.example.school.common.mysql.service.CommentService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.mysql.service.ZanService;
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

import javax.transaction.Transactional;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentReplyService commentReplyService;

    private final UserService userService;

    private final ZanService zanService;

    private final JPushTool jPushTool;

    @Override
    public Comment save(Comment comment) {
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
    public Optional<Comment> findByIdNotDelete(Long aLong) {
        return commentRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public Page<Comment> findPageByEntity(Comment comment) {
        List<SearchFilter> filters = getCommentFilter(comment);
        Specification<Comment> specification = SearchFilter.bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(comment);
        return commentRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId) {
//        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new Comment(toUserId, topicId, topicType, content));
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void adoptComment(Long id, Long topicId, Short topicType) {
        Comment comment = this.findComment(id);
        if (Objects.equal(comment.getState(), SysConst.CommentState.ADOPT.getType())) {
            throw new OperationException("已经被采纳了");
        }
        List<Comment> commentList = commentRepository.findByTopicIdAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        List<Comment> dbComment = commentList.parallelStream()
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
    public Comment findComment(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public Map<Long, Long> countComment(List<Long> topicId, Short topicType) {
        List<Comment> commentList = commentRepository.findByTopicIdInAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        return commentList.stream().collect(groupingBy(Comment::getTopicId, counting()));
    }

    @Override
    public PageImpl<RoCommentStatus> findRoCommentStatusPage(Comment comment, Long userId) {
        Page<Comment> commentPage = this.findPageByEntity(comment);
        return this.resultRoCommentStatus(commentPage, comment.getTopicType(), userId, Collections.emptyList());
    }

    @Override
    public PageImpl<RoCommentStatus> findRoCommentAndReplyStatusPage(Comment comment, Long userId) {
        Page<Comment> commentPage = this.findPageByEntity(comment);
        List<Long> commentIdList = commentPage.stream().map(Comment::getId).collect(toList());
        List<RoCommentReplyStatus> roCommentReplyStatusList = commentReplyService.findRoCommentReplyStatusList(commentIdList);
        return this.resultRoCommentStatus(commentPage, comment.getTopicType(), userId, roCommentReplyStatusList);

    }

    private RoTopicCommentMap getTopicCommentMethod(List<Long> userIdList, Long userId, List<Long> topicId, Short topicTyp) {
        Map<Long, RoUser> roUserMap = userService.findMapRoUserByUserId(userIdList);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, ZAN_COMMENT);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, ZAN_COMMENT);
        return new RoTopicCommentMap(roUserMap, zanNumMap, zanStateMap);
    }

    private PageImpl<RoCommentStatus> resultRoCommentStatus(Page<Comment> page, Short topicTyp, Long userId, List<RoCommentReplyStatus> roCommentReplyStatusList) {
        List<Long> topicId = page.stream().map(Comment::getId).collect(toList());
        List<Long> userIdList = page.stream().map(Comment::getUserId).distinct().collect(toList());
        RoTopicCommentMap topicCommentMap = getTopicCommentMethod(userIdList, userId, topicId, topicTyp);
        Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap = roCommentReplyStatusList.stream()
                .collect(groupingBy(RoCommentReplyStatus::getCommentId));
        List<RoCommentStatus> roTransactionList = RoChangeEntityUtils
                .resultRoCommentStatus(page.getContent(), userId, topicCommentMap, roCommentReplyStatusMap);
        return new PageImpl<>(roTransactionList, page.getPageable(), page.getTotalElements());
    }


    private List<SearchFilter> getCommentFilter(Comment comment) {
        List<SearchFilter> filters = Lists.newArrayList();
        filters.add(new SearchFilter("topicId", comment.getTopicId(), SearchFilter.Operator.EQ));
        filters.add(new SearchFilter("topicType", comment.getTopicType(), SearchFilter.Operator.EQ));
        filters.add(new SearchFilter("deleteState", UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }

}
