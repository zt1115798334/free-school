package com.example.school.app.controller;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageRecordTime;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.entity.CommentReply;
import com.example.school.common.mysql.entity.RecordTime;
import com.example.school.common.mysql.service.*;
import com.example.school.common.utils.change.VoChangeEntityUtils;
import com.example.school.shiro.aop.DistributedLock;
import com.example.school.shiro.aop.SaveLog;
import com.example.school.shiro.base.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.school.common.constant.SysConst.TopicType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 17:58
 * description:
 */
@Api(tags = "时光")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/recordTime")
public class RecordTimeController extends AbstractController implements CurrentUser, ConstantService {

    private final RecordTimeService recordTimeService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存时光信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveRecordTime")
    @SaveLog(desc = "保存时光信息")
    @DistributedLock
    public ResultMessage saveRecordTime(@Valid @RequestBody VoStorageRecordTime storageRecordTime) {
        RecordTime recordTime = VoChangeEntityUtils.changeStorageRecordTime(storageRecordTime);
        recordTime.setUserId(getCurrentUserId());
        RoRecordTime roRecordTime = recordTimeService.saveRecordTime(recordTime);
        return success("保存成功", roRecordTime);
    }


    @ApiOperation(value = "保存时光图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveRecordTimeImg")
    @SaveLog(desc = "保存时光图片信息")
    @DistributedLock
    public ResultMessage saveRecordTimeImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        recordTimeService.modifyRecordTimeSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, TOPIC_TYPE_5);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "删除时光信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteRecordTime")
    @SaveLog(desc = "删除时光信息")
    @DistributedLock
    public ResultMessage deleteRecordTime(@NotNull(message = "id不能为空") @RequestParam Long id) {
        recordTimeService.deleteRecordTime(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "查询时光信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTime")
    public ResultMessage findRecordTime(@NotNull(message = "id不能为空") @RequestParam Long id) {
        RoRecordTime recordTime = recordTimeService.findRoRecordTime(id, getCurrentUserId());
        return success(recordTime);
    }

    @ApiOperation(value = "查询有效的时光信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTimeEffective")
    public ResultMessage findRecordTimeEffective(@Valid @RequestBody VoParams params) {
        RecordTime recordTime = VoChangeEntityUtils.changeRecordTime(params);
        PageImpl<RoRecordTime> page = recordTimeService.findRecordTimeEffectivePage(recordTime, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户相关的时光信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTimeUser")
    public ResultMessage findRecordTimeUser(@Valid @RequestBody VoParams params) {
        RecordTime recordTime = VoChangeEntityUtils.changeRecordTime(params);
        Long currentUserId = getCurrentUserId();
        recordTime.setUserId(currentUserId);
        PageImpl<RoRecordTime> page = recordTimeService.findRecordTimeUserPage(recordTime, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户收藏的时光信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTimeCollection")
    public ResultMessage findRecordTimeCollection(@Valid @RequestBody VoPage voPage) {
        CustomPage customPage = VoChangeEntityUtils.changeIdPageEntity(voPage);
        PageImpl<RoRecordTime> page = recordTimeService.findRecordTimeCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存时光信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableRecordTimeZanOn")
    @SaveLog(desc = "保存时光信息点赞")
    @DistributedLock
    public ResultMessage enableRecordTimeZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                               @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_5, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存时光信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableRecordTimeZanOff")
    @SaveLog(desc = "保存时光信息取消点赞")
    @DistributedLock
    public ResultMessage enableRecordTimeZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_5, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存时光信息收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableRecordTimeCollectionOn")
    @SaveLog(desc = "保存时光信息收藏")
    @DistributedLock
    public ResultMessage enableRecordTimeCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOnCollection(getCurrentUserId(), id, TOPIC_TYPE_5);
        return success("保存成功");
    }

    @ApiOperation(value = "保存时光信息取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableRecordTimeCollectionOff")
    @SaveLog(desc = "保存时光信息取消收藏")
    @DistributedLock
    public ResultMessage enableRecordTimeCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOffCollection(getCurrentUserId(), id, TOPIC_TYPE_5);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "显示时光信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTimeComment")
    public ResultMessage findRecordTimeComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_5.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @ApiOperation(value = "显示时光信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTimeCommentReply")
    public ResultMessage findRecordTimeCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示时光信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findRecordTimeCommentAndReply")
    public ResultMessage findRecordTimeCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_5.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存时光信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveRecordTimeComment")
    @SaveLog(desc = "保存时光信息评论")
    @DistributedLock
    public ResultMessage saveRecordTimeComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                               @NotEmpty(message = "content不能为空") @RequestParam String content,
                                               @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, TOPIC_TYPE_5, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @ApiOperation(value = "保存时光信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveRecordTimeCommentReplyToComment")
    @SaveLog(desc = "保存时光信息回复")
    @DistributedLock
    public ResultMessage saveRecordTimeCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                             @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                             @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                             @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, TOPIC_TYPE_5, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存时光信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveRecordTimeCommentReplyToReply")
    @SaveLog(desc = "保存时光信息回复的回复")
    @DistributedLock
    public ResultMessage saveRecordTimeCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                           @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                           @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                           @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                           @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, TOPIC_TYPE_5, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存时光信息评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableRecordTimeCommentZanOn")
    @SaveLog(desc = "保存时光信息评论点赞")
    @DistributedLock
    public ResultMessage enableRecordTimeCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                      @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_5, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存时光信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableRecordTimeCommentZanOff")
    @SaveLog(desc = "保存时光信息点赞")
    @DistributedLock
    public ResultMessage enableRecordTimeCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                       @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_5, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }


}
