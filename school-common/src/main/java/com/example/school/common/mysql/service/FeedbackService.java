package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.ro.RoFeedback;
import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.Feedback;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
public interface FeedbackService extends Base<Feedback, Long> {

    RoFeedback saveFeedback(Feedback feedback);

    void saveFeedbackImg(HttpServletRequest request, Long feedbackId);

}
