package com.example.school.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoQuestionBank;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageQuestionBank;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.entity.CommentReply;
import com.example.school.common.mysql.entity.QuestionBank;
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
@Api(tags = "题库")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/questionBank")
public class QuestionBankController extends AbstractController implements CurrentUser, ConstantService {
    private final QuestionBankService questionBankService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveQuestionBank")
    @SaveLog(desc = "保存题库信息")
    @DistributedLock
    public ResultMessage saveQuestionBank(@Valid @RequestBody VoStorageQuestionBank storageQuestionBank) {
        QuestionBank questionBank = VoChangeEntityUtils.changeStorageQuestionBank(storageQuestionBank);
        questionBank.setUserId(getCurrentUserId());
        RoQuestionBank roQuestionBank = questionBankService.saveQuestionBank(questionBank);
        return success("保存成功", roQuestionBank);
    }


    @ApiOperation(value = "保存题库图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveQuestionBankImg")
    @SaveLog(desc = "保存题库图片信息")
    @DistributedLock
    public ResultMessage saveQuestionBankImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        questionBankService.modifyQuestionBankSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, TOPIC_TYPE_4);
        return success("保存成功");
    }

    @ApiOperation(value = "保存题库文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveQuestionBankFile")
    @SaveLog(desc = "保存题库文件信息")
    @DistributedLock
    public ResultMessage saveQuestionBankFile(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        questionBankService.modifyQuestionBankSateToNewRelease(topicId);
        questionBankService.saveQuestionBankFile(request, topicId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "删除题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteQuestionBank")
    @SaveLog(desc = "删除题库信息")
    @DistributedLock
    public ResultMessage deleteQuestionBank(@NotNull(message = "id不能为空") @RequestParam Long id) {
        questionBankService.deleteQuestionBank(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "查询题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBank")
    public ResultMessage findQuestionBank(@NotNull(message = "id不能为空") @RequestParam Long id) {
        RoQuestionBank questionBank = questionBankService.findRoQuestionBank(id, getCurrentUserId());
        return success(questionBank);
    }

    @ApiOperation(value = "查询有效的题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBankEffective")
    public ResultMessage findQuestionBankEffective(@Valid @RequestBody VoParams params) {
        QuestionBank questionBank = VoChangeEntityUtils.changeQuestionBank(params);
        PageImpl<RoQuestionBank> page = questionBankService.findQuestionBankEffectivePage(questionBank, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户相关的题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBankUser")
    public ResultMessage findQuestionBankUser(@Valid @RequestBody VoParams params) {
        QuestionBank questionBank = VoChangeEntityUtils.changeQuestionBank(params);
        Long currentUserId = getCurrentUserId();
        questionBank.setUserId(currentUserId);
        PageImpl<RoQuestionBank> page = questionBankService.findQuestionBankUserPage(questionBank, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户收藏的题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionCollection")
    public ResultMessage findQuestionCollection(@Valid @RequestBody VoPage voPage) {
        CustomPage customPage = VoChangeEntityUtils.changeIdPageEntity(voPage);
        PageImpl<RoQuestionBank> page = questionBankService.findQuestionBankCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询不含答案题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @GetMapping(value = "findQuestionBankNonExistentAnswer")
    public ResultMessage findQuestionBankNonExistentAnswer(@NotNull(message = "id不能为空") @RequestParam Long id) {
        JSONObject result = questionBankService.findQuestionBankNonExistentAnswer(id);
        return success(result);
    }

    @ApiOperation(value = "查询含答案题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @GetMapping(value = "findQuestionBankExistenceAnswer")
    public ResultMessage findQuestionBankExistenceAnswer(@NotNull(message = "id不能为空") @RequestParam Long id) {
        JSONObject result = questionBankService.findQuestionBankExistenceAnswer(id, getCurrentUserId());
        return success(result);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存题库信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableQuestionBankZanOn")
    @SaveLog(desc = "保存题库信息点赞")
    @DistributedLock
    public ResultMessage enableQuestionBankZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_4, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存题库信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableQuestionBankZanOff")
    @SaveLog(desc = "保存题库信息取消点赞")
    @DistributedLock
    public ResultMessage enableQuestionBankZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                  @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_4, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存题库信息收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableQuestionBankCollectionOn")
    @SaveLog(desc = "保存题库信息收藏")
    @DistributedLock
    public ResultMessage enableQuestionBankCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOnCollection(getCurrentUserId(), id, TOPIC_TYPE_4);
        return success("保存成功");
    }

    @ApiOperation(value = "保存题库信息取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableQuestionBankCollectionOff")
    @SaveLog(desc = "保存题库信息取消收藏")
    @DistributedLock
    public ResultMessage enableQuestionBankCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOffCollection(getCurrentUserId(), id, TOPIC_TYPE_4);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "显示题库信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBankComment")
    public ResultMessage findQuestionBankComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_4.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @ApiOperation(value = "显示题库信息评论数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBankCommentCount")
    public ResultMessage findQuestionBankCommentCount(@NotNull(message = "topicId不能为空") @RequestParam Long topicId) {
        JSONObject result = commentService.countComment(topicId, TopicType.TOPIC_TYPE_4.getCode());
        return success(result);
    }

    @ApiOperation(value = "显示题库信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBankCommentReply")
    public ResultMessage findQuestionBankCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示题库信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findQuestionBankCommentAndReply")
    public ResultMessage findQuestionBankCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_4.getCode());
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存题库信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveQuestionBankComment")
    @SaveLog(desc = "保存题库信息评论")
    @DistributedLock
    public ResultMessage saveQuestionBankComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                 @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, TOPIC_TYPE_4, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @ApiOperation(value = "保存题库信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveQuestionBankCommentReplyToComment")
    @SaveLog(desc = "保存题库信息回复")
    @DistributedLock
    public ResultMessage saveQuestionBankCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                               @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                               @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                               @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, TOPIC_TYPE_4, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存题库信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveQuestionBankCommentReplyToReply")
    @SaveLog(desc = "保存题库信息回复的回复")
    @DistributedLock
    public ResultMessage saveQuestionBankCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                             @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                             @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                             @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                             @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, TOPIC_TYPE_4, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存题库信息评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableQuestionBankCommentZanOn")
    @SaveLog(desc = "保存题库信息评论点赞")
    @DistributedLock
    public ResultMessage enableQuestionBankCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                        @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_4, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存题库信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableQuestionBankCommentZanOff")
    @SaveLog(desc = "保存题库信息点赞")
    @DistributedLock
    public ResultMessage enableQuestionBankCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                         @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_4, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

}
