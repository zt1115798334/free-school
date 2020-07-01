package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.ro.*;
import com.example.school.common.mysql.entity.*;
import com.example.school.common.mysql.service.*;
import com.example.school.common.mysql.service.CollectionService;
import com.example.school.common.mysql.service.CommentService;
import com.example.school.common.mysql.service.TopicImgService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.mysql.service.ZanService;
import com.example.school.common.utils.change.RoChangeEntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Component
public class TopicServiceImpl implements TopicService {

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final ZanService zanService;

    private final UserService userService;

    private final CollectionService collectionService;

    private RoTopicMap getTopicMethod(List<Long> userIdList, Long userId, List<Long> topicId, Short topicTyp) {
        Map<Long, RoUser> userMap = userService.findMapRoUserByUserId(userIdList);
        Map<Long, List<Long>> topicImgMap = topicImgService.findTopicImgList(topicId, topicTyp);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, ZAN_TOPIC);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, ZAN_TOPIC);
        Map<Long, List<RoUser>> zanUserMap = zanService.zanUser(topicId, topicTyp, ZAN_TOPIC);
        Map<Long, Long> commentCountMap = commentService.countComment(topicId, topicTyp);
        Map<Long, Long> collectionStateMap = collectionService.collectionState(userId, topicId, topicTyp);
        return new RoTopicMap(userMap, topicImgMap, zanNumMap, zanStateMap, collectionStateMap, commentCountMap, zanUserMap);
    }


    @Override
    public RoTransaction resultRoTransaction(Transaction transaction, Long userId) {
        List<Long> topicId = Collections.singletonList(transaction.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(transaction.getUserId()), userId, topicId, TOPIC_TYPE_1);
        return RoChangeEntityUtils.resultRoTransaction(transaction, userId, topicMethod);
    }

    @Override
    public PageImpl<RoTransaction> resultRoTransactionPage(Page<Transaction> page, Long userId) {
        List<Long> topicId = page.stream().map(Transaction::getId).collect(toList());
        List<Long> userIdList = page.stream().map(Transaction::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, TOPIC_TYPE_1);
        List<RoTransaction> roTransactionList = RoChangeEntityUtils
                .resultRoTransaction(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roTransactionList, page.getPageable(), page.getTotalElements());
    }

    @Override
    public RoRecordTime resultRoRecordTime(RecordTime recordTime, Long userId) {
        List<Long> topicId = Collections.singletonList(recordTime.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(recordTime.getUserId()), userId, topicId, TOPIC_TYPE_5);
        return RoChangeEntityUtils.resultRoRecordTime(recordTime, userId, topicMethod);
    }

    @Override
    public PageImpl<RoRecordTime> resultRoRecordTimePage(Page<RecordTime> page, Long userId) {
        List<Long> topicId = page.stream().map(RecordTime::getId).collect(toList());
        List<Long> userIdList = page.stream().map(RecordTime::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, TOPIC_TYPE_5);
        List<RoRecordTime> roRecordTimeList = RoChangeEntityUtils
                .resultRoRecordTime(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roRecordTimeList, page.getPageable(), page.getTotalElements());
    }

    @Override
    public RoKnowing resultRoKnowing(Knowing knowing, Long userId) {
        List<Long> topicId = Collections.singletonList(knowing.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(knowing.getUserId()), userId, topicId, TOPIC_TYPE_3);
        return RoChangeEntityUtils.resultRoKnowing(knowing, userId, topicMethod);

    }

    @Override
    public PageImpl<RoKnowing> resultRoKnowingPage(Page<Knowing> page, Long userId) {
        List<Long> topicId = page.stream().map(Knowing::getId).collect(toList());
        List<Long> userIdList = page.stream().map(Knowing::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, TOPIC_TYPE_3);
        List<RoKnowing> roKnowingList = RoChangeEntityUtils
                .resultRoKnowing(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roKnowingList, page.getPageable(), page.getTotalElements());

    }

    @Override
    public RoInformation resultRoInformation(Information information, Long userId) {
        List<Long> topicId = Collections.singletonList(information.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(information.getUserId()), userId, topicId, TOPIC_TYPE_2);
        return RoChangeEntityUtils.resultRoInformation(information, userId, topicMethod);

    }

    @Override
    public PageImpl<RoInformation> resultRoInformationPage(Page<Information> page, Long userId) {
        List<Long> topicId = page.stream().map(Information::getId).collect(toList());
        List<Long> userIdList = page.stream().map(Information::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, TOPIC_TYPE_2);
        List<RoInformation> roInformationList = RoChangeEntityUtils
                .resultRoInformation(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roInformationList, page.getPageable(), page.getTotalElements());
    }

    @Override
    public RoQuestionBank resultRoQuestionBank(QuestionBank questionBank, Long userId) {
        List<Long> topicId = Collections.singletonList(questionBank.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(questionBank.getUserId()), userId, topicId, TOPIC_TYPE_4);
        return RoChangeEntityUtils.resultRoQuestionBank(questionBank, userId, topicMethod);
    }

    @Override
    public PageImpl<RoQuestionBank> resultRoQuestionBankPage(Page<QuestionBank> page, Long userId) {
        List<Long> topicId = page.stream().map(QuestionBank::getId).collect(toList());
        List<Long> userIdList = page.stream().map(QuestionBank::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, TOPIC_TYPE_4);
        List<RoQuestionBank> roQuestionBankList = RoChangeEntityUtils
                .resultRoQuestionBank(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roQuestionBankList, page.getPageable(), page.getTotalElements());
    }

}
