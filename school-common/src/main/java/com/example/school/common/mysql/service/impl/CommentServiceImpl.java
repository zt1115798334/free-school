package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoTopicCommentMap;
import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.repo.CommentRepository;
import com.example.school.common.mysql.service.CommentReplyService;
import com.example.school.common.mysql.service.CommentService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.mysql.service.ZanService;
import com.example.school.common.tools.JPushTool;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.UserUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
@Transactional(rollbackOn = RuntimeException.class)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final UserService userService;

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
    public Page<Comment> findPageByEntity(Comment comment) {
        Map<String, SearchFilter> filters = getCommentFilter(comment);
        Specification<Comment> specification = SearchFilter.bySearchFilter(filters.values());
        Pageable pageable = PageUtils.buildPageRequest(comment);
        return commentRepository.findAll(specification, pageable);
    }

    @Override
    public Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId) {
        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new Comment(toUserId, topicId, topicType, content));
    }

    @Override
    public Map<Long, Long> countComment(List<Long> topicId, Short topicType) {
        List<Comment> commentList = commentRepository.findByTopicIdInAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        return commentList.stream().collect(groupingBy(Comment::getTopicId, counting()));
    }

    private RoTopicCommentMap getTopicCommentMethod(Comment comment) {
        Page<Comment> commentPage = this.findPageByEntity(comment);
        List<Long> userIdList = commentPage.stream().map(Comment::getUserId).collect(toList());
        Map<Long, RoUser> roUserMap = userService.findMapRoUserByUserId(userIdList);
        List<Long> topicId = commentPage.stream().map(Comment::getId).collect(toList());
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, comment.getTopicType(), ZAN_COMMENT);
        Map<Long, Long> zanStateMap = zanService.zanState(comment.getUserId(), topicId, comment.getTopicType(), ZAN_COMMENT);
        return new RoTopicCommentMap(commentPage, roUserMap, zanNumMap, zanStateMap);
    }

    @Override
    public CustomPage<RoCommentStatus> findRoCommentStatusPage(Comment comment, Long userId) {
        RoTopicCommentMap roTopicCommentMap = getTopicCommentMethod(comment);
        Page<Comment> commentPage = roTopicCommentMap.getCommentPage();
        List<RoCommentStatus> roCommentStatusList = commentPage.getContent().stream()
                .map(com -> new RoCommentStatus(
                        roTopicCommentMap.getRoUserMap().getOrDefault(com.getUserId(), UserUtils.getDefaultRoUser()),
                        roTopicCommentMap.getZanStateMap().containsKey(com.getUserId()),
                        roTopicCommentMap.getZanNumMap().getOrDefault(com.getId(), 0L),
                        com.getId(), com.getTopicId(), com.getContent(), DateUtils.formatDate(com.getCreatedTime())))
                .collect(toList());
        return new CustomPage<>(commentPage.getNumber(), comment.getPageSize(), roCommentStatusList, commentPage.getTotalElements());
    }

    @Override
    public CustomPage<RoCommentStatus> findRoCommentAndReplyStatusPage(Comment comment, Long userId) {
        RoTopicCommentMap roTopicCommentMap = getTopicCommentMethod(comment);
        Page<Comment> commentPage = roTopicCommentMap.getCommentPage();
        List<Long> commentIdList = commentPage.stream().map(Comment::getId).collect(toList());
        List<RoCommentReplyStatus> roCommentReplyStatusList = commentReplyService.findRoCommentReplyStatusList(commentIdList);
        Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap = roCommentReplyStatusList.stream().collect(groupingBy(RoCommentReplyStatus::getCommentId));

        List<RoCommentStatus> roCommentStatusList = commentPage.getContent().stream()
                .map(com -> new RoCommentStatus(
                        roTopicCommentMap.getRoUserMap().getOrDefault(com.getUserId(), UserUtils.getDefaultRoUser()),
                        roTopicCommentMap.getZanStateMap().containsKey(com.getUserId()),
                        roTopicCommentMap.getZanNumMap().getOrDefault(com.getId(), 0L),
                        com.getId(), com.getTopicId(), com.getContent(), DateUtils.formatDate(com.getCreatedTime()),
                        roCommentReplyStatusMap.getOrDefault(com.getId(), Collections.emptyList())))
                .collect(toList());
        return new CustomPage<>(commentPage.getNumber(), commentPage.getSize(), roCommentStatusList, commentPage.getTotalElements());

    }

    private Map<String, SearchFilter> getCommentFilter(Comment comment) {
        Map<String, SearchFilter> filters = Maps.newHashMap();
        filters.put("topicId", new SearchFilter("topicId", comment.getTopicId(), SearchFilter.Operator.EQ));
        filters.put("topicType", new SearchFilter("topicType", comment.getTopicType(), SearchFilter.Operator.EQ));
        filters.put("deleteState", new SearchFilter("deleteState", UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }

}
