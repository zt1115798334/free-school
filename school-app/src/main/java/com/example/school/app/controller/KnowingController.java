package com.example.school.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoKnowing;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageKnowing;
import com.example.school.common.base.service.Constant;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.entity.CommentReply;
import com.example.school.common.mysql.entity.Knowing;
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
@Api(tags = "问答")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/knowing")
public class KnowingController extends AbstractController implements CurrentUser, Constant {
    private final KnowingService knowingService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存问答信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveKnowing")
    @SaveLog(desc = "保存问答信息")
    @DistributedLock
    public ResultMessage saveKnowing(@Valid @RequestBody VoStorageKnowing storageKnowing) {
        Knowing knowing = VoChangeEntityUtils.changeStorageKnowing(storageKnowing);
        RoKnowing roKnowing = knowingService.saveKnowing(knowing, getCurrentUserId());
        return success("保存成功", roKnowing);
    }


    @ApiOperation(value = "保存问答图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveKnowingImg")
    @SaveLog(desc = "保存问答图片信息")
    @DistributedLock
    public ResultMessage saveKnowingImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        knowingService.modifyKnowingSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, TOPIC_TYPE_3);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "删除问答信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteKnowing")
    @SaveLog(desc = "删除问答信息")
    @DistributedLock
    public ResultMessage deleteKnowing(@NotNull(message = "id不能为空") @RequestParam Long id) {
        knowingService.deleteKnowing(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "查询问答信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowing")
    public ResultMessage findKnowing(@NotNull(message = "id不能为空") @RequestParam Long id) {
        RoKnowing knowing = knowingService.findRoKnowing(id, getCurrentUserId());
        return success(knowing);
    }

    @ApiOperation(value = "查询有效的问答信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingEffective")
    public ResultMessage findKnowingEffective(@Valid @RequestBody VoParams params) {
        Knowing knowing = VoChangeEntityUtils.changeKnowing(params);
        PageImpl<RoKnowing> page = knowingService.findKnowingEffectivePage(knowing, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户相关的问答信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingUser")
    public ResultMessage findKnowingUser(@Valid @RequestBody VoParams params) {
        Knowing knowing = VoChangeEntityUtils.changeKnowing(params);
        Long currentUserId = getCurrentUserId();
        knowing.setUserId(currentUserId);
        PageImpl<RoKnowing> page = knowingService.findKnowingUserPage(knowing, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户收藏的问答信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingCollection")
    public ResultMessage findKnowingCollection(@Valid @RequestBody VoPage voPage) {
        CustomPage customPage = VoChangeEntityUtils.changeIdPageEntity(voPage);
        PageImpl<RoKnowing> page = knowingService.findKnowingCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存问答信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableKnowingZanOn")
    @SaveLog(desc = "保存问答信息点赞")
    @DistributedLock
    public ResultMessage enableKnowingZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_3, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存问答信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableKnowingZanOff")
    @SaveLog(desc = "保存问答信息取消点赞")
    @DistributedLock
    public ResultMessage enableKnowingZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                             @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_3, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存问答信息收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableKnowingCollectionOn")
    @SaveLog(desc = "保存问答信息收藏")
    @DistributedLock
    public ResultMessage enableKnowingCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOnCollection(getCurrentUserId(), id, TOPIC_TYPE_3);
        return success("保存成功");
    }

    @ApiOperation(value = "保存问答信息取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableKnowingCollectionOff")
    @SaveLog(desc = "保存问答信息取消收藏")
    @DistributedLock
    public ResultMessage enableKnowingCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOffCollection(getCurrentUserId(), id, TOPIC_TYPE_3);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "显示问答信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingComment")
    public ResultMessage findKnowingComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_3.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @ApiOperation(value = "显示问答信息评论数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingCommentCount")
    public ResultMessage findKnowingCommentCount(@NotNull(message = "topicId不能为空") @RequestParam Long topicId) {
        JSONObject result = commentService.countComment(topicId, TopicType.TOPIC_TYPE_3.getCode());
        return success(result);
    }

    @ApiOperation(value = "显示问答信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingCommentReply")
    public ResultMessage findKnowingCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示问答信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findKnowingCommentAndReply")
    public ResultMessage findKnowingCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_3.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存问答信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveKnowingComment")
    @SaveLog(desc = "保存问答信息评论")
    @DistributedLock
    public ResultMessage saveKnowingComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                            @NotEmpty(message = "content不能为空") @RequestParam String content,
                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, TOPIC_TYPE_3, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @ApiOperation(value = "保存问答信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveKnowingCommentReplyToComment")
    @SaveLog(desc = "保存问答信息回复")
    @DistributedLock
    public ResultMessage saveKnowingCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                          @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                          @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                          @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, TOPIC_TYPE_3, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存问答信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveKnowingCommentReplyToReply")
    @SaveLog(desc = "保存问答信息回复的回复")
    @DistributedLock
    public ResultMessage saveKnowingCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                        @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                        @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                        @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                        @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, TOPIC_TYPE_3, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存问答信息评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableKnowingCommentZanOn")
    @SaveLog(desc = "保存问答信息评论点赞")
    @DistributedLock
    public ResultMessage enableKnowingCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                   @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_3, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存问答信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableKnowingCommentZanOff")
    @SaveLog(desc = "保存问答信息点赞")
    @DistributedLock
    public ResultMessage enableKnowingCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                    @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_3, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "采纳问答信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "adoptKnowingComment")
    @SaveLog(desc = "采纳问答信息评论")
    @DistributedLock
    public ResultMessage adoptKnowingComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                             @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                             @NotNull(message = "commentUserId不能为空") @RequestParam Long commentUserId) {
        knowingService.adoptKnowingComment(topicId, getCurrentUserId(), commentId, commentUserId);
        return success("保存成功");
    }


}
