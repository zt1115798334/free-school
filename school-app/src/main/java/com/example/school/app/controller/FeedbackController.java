package com.example.school.app.controller;

import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoFeedback;
import com.example.school.common.base.entity.vo.VoStorageFeedback;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.mysql.entity.Feedback;
import com.example.school.common.mysql.service.FeedbackService;
import com.example.school.common.utils.change.VoChangeEntityUtils;
import com.example.school.shiro.aop.DistributedLock;
import com.example.school.shiro.aop.SaveLog;
import com.example.school.shiro.base.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/7/12 15:37
 * description:
 */
@Api(tags = "反馈")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping(value = "app/feedback")
public class FeedbackController extends AbstractController implements CurrentUser {

    private FeedbackService feedbackService;

    @ApiOperation(value = "保存反馈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveFeedback")
    @SaveLog(desc = "保存反馈信息")
    @DistributedLock
    public ResultMessage saveFeedback(@Valid @RequestBody VoStorageFeedback storageFeedback) {
        Feedback feedback = VoChangeEntityUtils.changeStorageFeedback(storageFeedback);
        Long currentUserId = getCurrentUserId();
        feedback.setUserId(currentUserId);
        RoFeedback roFeedback = feedbackService.saveFeedback(feedback);
        return success("保存成功", roFeedback);
    }


    @ApiOperation(value = "保存反馈图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveFeedbackImg")
    @SaveLog(desc = "保存反馈图片信息")
    @DistributedLock
    public ResultMessage saveFeedbackImg(HttpServletRequest request) {
        Long feedbackId = Long.valueOf(request.getParameter("feedbackId"));
        feedbackService.saveFeedbackImg(request, feedbackId);
        return success("保存成功");
    }
}
