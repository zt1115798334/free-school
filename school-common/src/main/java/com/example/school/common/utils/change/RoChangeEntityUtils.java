package com.example.school.common.utils.change;

import com.example.school.common.base.entity.ro.*;
import com.example.school.common.mysql.entity.RecordTime;
import com.example.school.common.mysql.entity.Transaction;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.entity.UserImg;
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
                new RoImagePath(userImgUrl.getImgId()));
    }

    public static List<RoUser> resultRoUser(List<User> userList, Map<Long, UserImg> userImgMap) {
        return userList.stream().map(user ->
                RoChangeEntityUtils.resultRoUser(user,
                        userImgMap.getOrDefault(user.getId(), UserUtils.getDefaultUserImg())))
                .collect(toList());
    }

    public static RoTransaction resultRoTransaction(Transaction transaction, Long userId,
                                                    RoTopicMap topicMethod) {
        return new RoTransaction(
                topicMethod.getRoUser(),
                transaction.getState(),
                Objects.equal(transaction.getUserId(), userId),
                topicMethod.getZanStateMap().containsKey(transaction.getId()),
                topicMethod.getZanNumMap().getOrDefault(transaction.getId(), 0L),
                topicMethod.getZanUserMap().getOrDefault(transaction.getId(), Collections.emptyList()),
                topicMethod.getCommentCountMap().getOrDefault(transaction.getId(), 0L),
                topicMethod.getTopicImgMap().getOrDefault(transaction.getId(), Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                transaction.getId(),
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
        return new RoRecordTime(
                topicMethod.getRoUser(),
                recordTime.getState(),
                Objects.equal(recordTime.getUserId(), userId),
                topicMethod.getZanStateMap().containsKey(recordTime.getId()),
                topicMethod.getZanNumMap().getOrDefault(recordTime.getId(), 0L),
                topicMethod.getZanUserMap().getOrDefault(recordTime.getId(), Collections.emptyList()),
                topicMethod.getCommentCountMap().getOrDefault(recordTime.getId(), 0L),
                topicMethod.getTopicImgMap().getOrDefault(recordTime.getId(), Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                recordTime.getId(),
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
}
