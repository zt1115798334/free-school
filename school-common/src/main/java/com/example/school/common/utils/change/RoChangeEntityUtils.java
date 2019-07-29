package com.example.school.common.utils.change;

import com.example.school.common.base.entity.ro.*;
import com.example.school.common.mysql.entity.*;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.UserUtils;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:08
 * description:
 */
public class RoChangeEntityUtils {

    public static RoUser resultRoUser(User user, UserImg userImgUrl) {
        return new RoUser(
                user.getId(),
                user.getUserName(),
                user.getPersonalSignature(),
                user.getPhone(),
                user.getEmail(),
                user.getSex(),
                user.getIntegral(),
                user.getSchool(),
                user.getAccountType(),
                new RoImagePath(userImgUrl.getImgId()));
    }

    public static List<RoUser> resultRoUser(List<User> userList, Map<Long, UserImg> userImgMap) {
        return userList.stream().map(user ->
                RoChangeEntityUtils.resultRoUser(user,
                        userImgMap.getOrDefault(user.getId(), UserUtils.getDefaultUserImg())))
                .collect(toList());
    }


    public static RoCommentStatus resultRoCommentStatus(Comment comment,
                                                        Long userId,
                                                        RoTopicCommentMap topicCommentMap,
                                                        Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap) {
        Long commentId = comment.getId();
        Long commentUserId = comment.getUserId();
        return new RoCommentStatus(
                topicCommentMap.getUserMap().getOrDefault(commentUserId, UserUtils.getDefaultRoUser()),
                comment.getState(),
                Objects.equal(commentUserId, userId),
                topicCommentMap.getZanStateMap().containsKey(commentId),
                topicCommentMap.getZanNumMap().getOrDefault(commentId, 0L),
                commentId,
                comment.getTopicId(),
                comment.getContent(),
                DateUtils.formatDate(comment.getCreatedTime()),
                roCommentReplyStatusMap.getOrDefault(commentId,Collections.emptyList()));
    }

    public static List<RoCommentStatus> resultRoCommentStatus(List<Comment> commentList,
                                                              Long userId,
                                                              RoTopicCommentMap topicCommentMap,
                                                              Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap) {
        return commentList.stream()
                .map(comment -> resultRoCommentStatus(comment, userId, topicCommentMap, roCommentReplyStatusMap)).collect(toList());
    }

    public static RoTransaction resultRoTransaction(Transaction transaction, Long userId,
                                                    RoTopicMap topicMethod) {
        Long transactionId = transaction.getId();
        Long transactionUserId = transaction.getUserId();
        return new RoTransaction(
                topicMethod.getUserMap().getOrDefault(transactionUserId, UserUtils.getDefaultRoUser()),
                transaction.getState(),
                Objects.equal(transactionUserId, userId),
                topicMethod.getZanStateMap().containsKey(transactionId),
                topicMethod.getZanNumMap().getOrDefault(transactionId, 0L),
                topicMethod.getZanUserMap().getOrDefault(transactionId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(transactionId),
                topicMethod.getCommentCountMap().getOrDefault(transactionId, 0L),
                transaction.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(transactionId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),

                transactionId,
                transaction.getTitle(),
                DateUtils.formatDateTime(transaction.getCreatedTime()),
                transaction.getPrice(),
                transaction.getDescribeContent(),
                transaction.getContactMode(),
                transaction.getContactPeople(),
                transaction.getAddress());
    }

    public static List<RoTransaction> resultRoTransaction(List<Transaction> transactionList,
                                                          Long userId,
                                                          RoTopicMap topicMethod) {
        return transactionList.stream()
                .map(transaction ->
                        resultRoTransaction(transaction, userId, topicMethod)).collect(toList());
    }

    public static RoRecordTime resultRoRecordTime(RecordTime recordTime, Long userId,
                                                  RoTopicMap topicMethod) {
        Long recordTimeId = recordTime.getId();
        Long recordTimeUserId = recordTime.getUserId();
        return new RoRecordTime(
                topicMethod.getUserMap().getOrDefault(recordTimeUserId, UserUtils.getDefaultRoUser()),
                recordTime.getState(),
                Objects.equal(recordTimeUserId, userId),
                topicMethod.getZanStateMap().containsKey(recordTimeId),
                topicMethod.getZanNumMap().getOrDefault(recordTimeId, 0L),
                topicMethod.getZanUserMap().getOrDefault(recordTimeId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(recordTimeId),
                topicMethod.getCommentCountMap().getOrDefault(recordTimeId, 0L),
                recordTime.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(recordTimeId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                recordTimeId,
                recordTime.getTitle(),
                DateUtils.formatDateTime(recordTime.getCreatedTime()),
                recordTime.getDescribeContent());
    }

    public static List<RoRecordTime> resultRoRecordTime(List<RecordTime> recordTimeList,
                                                        Long userId,
                                                        RoTopicMap topicMethod) {
        return recordTimeList.stream()
                .map(recordTime ->
                        resultRoRecordTime(recordTime, userId, topicMethod)).collect(toList());
    }

    public static RoKnowing resultRoKnowing(Knowing knowing, Long userId,
                                            RoTopicMap topicMethod) {
        Long knowingId = knowing.getId();
        Long KnowingUserId = knowing.getUserId();
        return new RoKnowing(
                topicMethod.getUserMap().getOrDefault(KnowingUserId, UserUtils.getDefaultRoUser()),
                knowing.getState(),
                Objects.equal(KnowingUserId, userId),
                topicMethod.getZanStateMap().containsKey(knowingId),
                topicMethod.getZanNumMap().getOrDefault(knowingId, 0L),
                topicMethod.getZanUserMap().getOrDefault(knowingId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(knowingId),
                topicMethod.getCommentCountMap().getOrDefault(knowingId, 0L),
                knowing.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(knowingId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                knowingId,
                knowing.getTitle(),
                DateUtils.formatDateTime(knowing.getCreatedTime()),
                knowing.getDescribeContent(),
                knowing.getIntegral());
    }

    public static List<RoKnowing> resultRoKnowing(List<Knowing> knowingList,
                                                  Long userId,
                                                  RoTopicMap topicMethod) {
        return knowingList.stream()
                .map(Knowing ->
                        resultRoKnowing(Knowing, userId, topicMethod)).collect(toList());
    }


    public static RoInformation resultRoInformation(Information information, Long userId,
                                                    RoTopicMap topicMethod) {
        Long informationId = information.getId();
        Long informationUserId = information.getUserId();
        return new RoInformation(
                topicMethod.getUserMap().getOrDefault(informationUserId, UserUtils.getDefaultRoUser()),
                information.getState(),
                Objects.equal(informationUserId, userId),
                topicMethod.getZanStateMap().containsKey(informationId),
                topicMethod.getZanNumMap().getOrDefault(informationId, 0L),
                topicMethod.getZanUserMap().getOrDefault(informationId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(informationId),
                topicMethod.getCommentCountMap().getOrDefault(informationId, 0L),
                information.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(informationId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                informationId,
                information.getTitle(),
                DateUtils.formatDateTime(information.getCreatedTime()),
                information.getDescribeContent());
    }

    public static List<RoInformation> resultRoInformation(List<Information> informationList,
                                                          Long userId,
                                                          RoTopicMap topicMethod) {
        return informationList.stream()
                .map(information ->
                        resultRoInformation(information, userId, topicMethod)).collect(toList());
    }

    public static RoQuestionBank resultRoQuestionBank(QuestionBank questionBank, Long userId,
                                                      RoTopicMap topicMethod) {
        Long questionBankId = questionBank.getId();
        Long questionBankUserId = questionBank.getUserId();
        return new RoQuestionBank(
                topicMethod.getUserMap().getOrDefault(questionBankUserId, UserUtils.getDefaultRoUser()),
                questionBank.getState(),
                Objects.equal(questionBankUserId, userId),
                topicMethod.getZanStateMap().containsKey(questionBankId),
                topicMethod.getZanNumMap().getOrDefault(questionBankId, 0L),
                topicMethod.getZanUserMap().getOrDefault(questionBankId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(questionBankId),
                topicMethod.getCommentCountMap().getOrDefault(questionBankId, 0L),
                questionBank.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(questionBankId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                questionBankId,
                questionBank.getTitle(),
                DateUtils.formatDateTime(questionBank.getCreatedTime()),
                questionBank.getDescribeContent());
    }

    public static List<RoQuestionBank> resultRoQuestionBank(List<QuestionBank> questionBankList,
                                                            Long userId,
                                                            RoTopicMap topicMethod) {
        return questionBankList.stream()
                .map(questionBank ->
                        resultRoQuestionBank(questionBank, userId, topicMethod)).collect(toList());
    }

    public static RoFeedback resultRoFeedback(Feedback feedback, Map<Long, List<Long>> feedbackImgMap) {
        return new RoFeedback(
                feedback.getFeedbackType(),
                feedback.getContent(),
                feedback.getContactMode(),
                feedbackImgMap.getOrDefault(feedback.getId(), Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()));
    }

    public static List<RoFeedback> resultRoFeedback(List<Feedback> feedbackList, Map<Long, List<Long>> feedbackImgMap) {
        return feedbackList.stream()
                .map(feedback -> resultRoFeedback(feedback, feedbackImgMap)).collect(toList());
    }
}
