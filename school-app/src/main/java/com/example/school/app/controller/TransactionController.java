package com.example.school.app.controller;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoCommentReplyStatus;
import com.example.school.common.base.entity.ro.RoCommentStatus;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageTransaction;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Comment;
import com.example.school.common.mysql.entity.CommentReply;
import com.example.school.common.mysql.entity.Transaction;
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
@Api(tags = "交易")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/transaction")
public class TransactionController extends AbstractController implements CurrentUser, ConstantService {

    private final TransactionService transactionService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    @ApiOperation(value = "保存交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveTransaction")
    @SaveLog(desc = "保存交易信息")
    @DistributedLock
    public ResultMessage saveTransaction(@Valid @RequestBody VoStorageTransaction storageTransaction) {
        Transaction transaction = VoChangeEntityUtils.changeStorageTransaction(storageTransaction);
        Long currentUserId = getCurrentUserId();
        transaction.setUserId(currentUserId);
        RoTransaction roTransaction = transactionService.saveTransaction(transaction);
        return success("保存成功", roTransaction);
    }


    @ApiOperation(value = "保存交易图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveTransactionImg")
    @SaveLog(desc = "保存交易图片信息")
    @DistributedLock
    public ResultMessage saveTransactionImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        transactionService.modifyTransactionSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, TOPIC_TYPE_1);
        return success("保存成功");
    }

    @ApiOperation(value = "删除交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteTransaction")
    @SaveLog(desc = "删除交易信息")
    @DistributedLock
    public ResultMessage deleteTransaction(@NotNull(message = "id不能为空") @RequestParam Long id) {
        transactionService.deleteTransaction(id);
        return success("删除成功");
    }

    @ApiOperation(value = "查询交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findTransaction")
    public ResultMessage findTransaction(@NotNull(message = "id不能为空") @RequestParam Long id) throws OperationException {
        RoTransaction transaction = transactionService.findTransaction(id, getCurrentUserId());
        return success(transaction);
    }

    @ApiOperation(value = "查询有效的交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findTransactionEffective")
    public ResultMessage findTransactionEffective(@Valid @RequestBody VoParams params) {
        Transaction transaction = VoChangeEntityUtils.changeTransaction(params);
        CustomPage<RoTransaction> page = transactionService.findTransactionEffectivePage(transaction, getCurrentUserId());
        return success(page.getPageNumber(), page.getPageSize(), page.getTotalElements(), page.getList());
    }

    @ApiOperation(value = "查询用户相关的交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findTransactionUser")
    public ResultMessage findTransactionUser(@Valid @RequestBody VoParams params) {
        Transaction transaction = VoChangeEntityUtils.changeTransaction(params);
        Long currentUserId = getCurrentUserId();
        transaction.setUserId(currentUserId);
        CustomPage<RoTransaction> page = transactionService.findTransactionUserPage(transaction, getCurrentUserId());
        return success(page.getPageNumber(), page.getPageSize(), page.getTotalElements(), page.getList());
    }

    @ApiOperation(value = "保存交易信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableTransactionZanOn")
    @SaveLog(desc = "保存交易信息点赞")
    @DistributedLock
    public ResultMessage enableTransactionZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_1, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存交易信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableTransactionZanOff")
    @SaveLog(desc = "保存交易信息取消点赞")
    @DistributedLock
    public ResultMessage enableTransactionZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_1, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }


    @ApiOperation(value = "显示交易信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findTransactionComment")
    public ResultMessage findTransactionComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_1.getCode());
        CustomPage<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(voCommentPage.getPageNumber(), voCommentPage.getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getList());
    }

    @ApiOperation(value = "显示交易信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findTransactionCommentReply")
    public ResultMessage findTransactionCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示交易信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findTransactionCommentAndReply")
    public ResultMessage findTransactionCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_1.getCode());
        CustomPage<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(voCommentPage.getPageNumber(), voCommentPage.getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getList());
    }

    @ApiOperation(value = "保存交易信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveTransactionComment")
    @SaveLog(desc = "保存交易信息评论")
    @DistributedLock
    public ResultMessage saveTransactionComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, TOPIC_TYPE_1, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @PostMapping(value = "enableTransactionCommentZanOn")
    @SaveLog(desc = "保存交易信息评论点赞")
    @DistributedLock
    public ResultMessage enableTransactionCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                       @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, TOPIC_TYPE_1, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存交易信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableTransactionCommentZanOff")
    @SaveLog(desc = "保存交易信息点赞")
    @DistributedLock
    public ResultMessage enableTransactionCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                        @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, TOPIC_TYPE_1, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存交易信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveTransactionCommentReplyToComment")
    @SaveLog(desc = "保存交易信息回复")
    @DistributedLock
    public ResultMessage saveTransactionCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                              @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                              @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                              @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, TOPIC_TYPE_1, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存交易信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveTransactionCommentReplyToReply")
    @SaveLog(desc = "保存交易信息回复的回复")
    @DistributedLock
    public ResultMessage saveTransactionCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                            @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                            @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                            @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, TOPIC_TYPE_1, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

}
