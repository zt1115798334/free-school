package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.entity.ro.RoTopicMap;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.mysql.entity.RecordTime;
import com.example.school.common.mysql.entity.Transaction;
import com.example.school.common.mysql.service.*;
import com.example.school.common.utils.change.RoChangeEntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TopicServiceImpl implements TopicService {

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final ZanService zanService;

    private final UserService userService;

    private RoTopicMap getTopicMethod(Long userId, List<Long> topicId, Short topicTyp, Short zanType) {
        RoUser user = userService.findRoUserByUserId(userId);
        Map<Long, List<Long>> topicImgMap = topicImgService.findTopicImgList(topicId, topicTyp);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, zanType);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, zanType);
        Map<Long, List<RoUser>> zanUserMap = zanService.zanUser(topicId, topicTyp, zanType);
        Map<Long, Long> commentCountMap = commentService.countComment(topicId, topicTyp);
        return new RoTopicMap(user, topicImgMap, zanNumMap, zanStateMap, zanUserMap, commentCountMap);
    }

    @Override
    public RoTransaction resultRoTransaction(Transaction transaction, Long userId, Short topicTyp, Short zanType) {
        List<Long> topicId = Collections.singletonList(transaction.getId());
        RoTopicMap topicMethod = getTopicMethod(userId, topicId, topicTyp, zanType);
        return RoChangeEntityUtils.resultRoTransaction(transaction, userId, topicMethod);
    }

    @Override
    public CustomPage<RoTransaction> resultRoTransactionPage(Page<Transaction> page, Long userId, Short topicTyp, Short zanType) {
        List<Long> topicId = page.stream().map(Transaction::getId).collect(Collectors.toList());
        RoTopicMap topicMethod = getTopicMethod(userId, topicId, topicTyp, zanType);
        List<RoTransaction> roTransactions = RoChangeEntityUtils
                .resultRoTransaction(page.getContent(), userId, topicMethod);
        return new CustomPage<>(page.getNumber(), page.getSize(), roTransactions, page.getTotalElements());
    }

    @Override
    public RoRecordTime resultRoRecordTime(RecordTime recordTime, Long userId, Short topicTyp, Short zanType) {
        List<Long> topicId = Collections.singletonList(recordTime.getId());
        RoTopicMap topicMethod = getTopicMethod(userId, topicId, topicTyp, zanType);
        return RoChangeEntityUtils.resultRoRecordTime(recordTime, userId, topicMethod);
    }

    @Override
    public CustomPage<RoRecordTime> resultRoRecordTimePage(Page<RecordTime> page, Long userId, Short topicTyp, Short zanType) {

        List<Long> topicId = page.stream().map(RecordTime::getId).collect(Collectors.toList());
        RoTopicMap topicMethod = getTopicMethod(userId, topicId, topicTyp, zanType);
        List<RoRecordTime> roRecordTimes = RoChangeEntityUtils
                .resultRoRecordTime(page.getContent(), userId, topicMethod);
        return new CustomPage<>(page.getNumber(), page.getSize(), roRecordTimes, page.getTotalElements());
    }


}
