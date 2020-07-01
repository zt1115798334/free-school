package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
public interface FeedbackImg extends Base<com.example.school.common.mysql.entity.FeedbackImg, Long> {

    Map<Long, List<Long>> findByFeedbackId(Long feedbackId);

    Map<Long, List<Long>>  findByFeedbackIds(List<Long> feedbackIds);

}
