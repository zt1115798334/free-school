package com.example.school.app.controller;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoInformation;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageInformation;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.entity.CommentReply;
import com.example.school.common.mysql.entity.Information;
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
@Api(tags = "资讯")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/information")
public class InformationController extends AbstractController implements CurrentUser, ConstantService {

    private final InformationService informationService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存资讯信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveInformation")
    @SaveLog(desc = "保存资讯信息")
    @DistributedLock
    public ResultMessage saveInformation(@Valid @RequestBody VoStorageInformation storageInformation) {
        Information information = VoChangeEntityUtils.changeStorageInformation(storageInformation);
        information.setUserId(getCurrentUserId());
        RoInformation roInformation = informationService.saveInformation(information);
        return success("保存成功", roInformation);
    }


    @ApiOperation(value = "保存资讯图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveInformationImg")
    @SaveLog(desc = "保存资讯图片信息")
    @DistributedLock
    public ResultMessage saveInformationImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        informationService.modifyInformationSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, TOPIC_TYPE_2);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "删除资讯信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteInformation")
    @SaveLog(desc = "删除资讯信息")
    @DistributedLock
    public ResultMessage deleteInformation(@NotNull(message = "id不能为空") @RequestParam Long id) {
        informationService.deleteInformation(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "查询资讯信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformation")
    public ResultMessage findInformation(@NotNull(message = "id不能为空") @RequestParam Long id) {
        RoInformation information = informationService.findRoInformation(id, getCurrentUserId());
        return success(information);
    }

    @ApiOperation(value = "查询有效的资讯信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformationEffective")
    public ResultMessage findInformationEffective(@Valid @RequestBody VoParams params) {
        Information information = VoChangeEntityUtils.changeInformation(params);
        PageImpl<RoInformation> page = informationService.findInformationEffectivePage(information, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户相关的资讯信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformationUser")
    public ResultMessage findInformationUser(@Valid @RequestBody VoParams params) {
        Information information = VoChangeEntityUtils.changeInformation(params);
        Long currentUserId = getCurrentUserId();
        information.setUserId(currentUserId);
        PageImpl<RoInformation> page = informationService.findInformationUserPage(information, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }


    @ApiOperation(value = "查询用户收藏的资讯信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformationCollection")
    public ResultMessage findInformationCollection(@Valid @RequestBody VoPage voPage) {
        CustomPage customPage = VoChangeEntityUtils.changeIdPageEntity(voPage);
        PageImpl<RoInformation> page = informationService.findInformationCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存资讯信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableInformationZanOn")
    @SaveLog(desc = "保存资讯信息点赞")
    @DistributedLock
    public ResultMessage enableInformationZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_2, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存资讯信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableInformationZanOff")
    @SaveLog(desc = "保存资讯信息取消点赞")
    @DistributedLock
    public ResultMessage enableInformationZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_2, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存资讯信息收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableInformationCollectionOn")
    @SaveLog(desc = "保存资讯信息收藏")
    @DistributedLock
    public ResultMessage enableInformationCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOnCollection(getCurrentUserId(), id, TOPIC_TYPE_2);
        return success("保存成功");
    }

    @ApiOperation(value = "保存资讯信息取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableInformationCollectionOff")
    @SaveLog(desc = "保存资讯信息取消收藏")
    @DistributedLock
    public ResultMessage enableInformationCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOffCollection(getCurrentUserId(), id, TOPIC_TYPE_2);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "显示资讯信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformationComment")
    public ResultMessage findInformationComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_2.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @ApiOperation(value = "显示资讯信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformationCommentReply")
    public ResultMessage findInformationCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示资讯信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findInformationCommentAndReply")
    public ResultMessage findInformationCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_2.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存资讯信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveInformationComment")
    @SaveLog(desc = "保存资讯信息评论")
    @DistributedLock
    public ResultMessage saveInformationComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, TOPIC_TYPE_2, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @ApiOperation(value = "保存资讯信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveInformationCommentReplyToComment")
    @SaveLog(desc = "保存资讯信息回复")
    @DistributedLock
    public ResultMessage saveInformationCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                              @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                              @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                              @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, TOPIC_TYPE_2, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存资讯信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveInformationCommentReplyToReply")
    @SaveLog(desc = "保存资讯信息回复的回复")
    @DistributedLock
    public ResultMessage saveInformationCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                            @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                            @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                            @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, TOPIC_TYPE_2, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存资讯信息评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableInformationCommentZanOn")
    @SaveLog(desc = "保存资讯信息评论点赞")
    @DistributedLock
    public ResultMessage enableInformationCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                       @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_2, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存资讯信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableInformationCommentZanOff")
    @SaveLog(desc = "保存资讯信息点赞")
    @DistributedLock
    public ResultMessage enableInformationCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                        @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_2, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }


}
