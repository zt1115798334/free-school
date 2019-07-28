package com.example.school.common.utils.change;

import com.example.school.common.base.entity.vo.*;
import com.example.school.common.mysql.entity.*;
import com.example.school.common.utils.DateUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:04
 * description:
 */
public class VoChangeEntityUtils {

    public static User changeUser(VoStorageUser voStorageUser) {
        return new User(voStorageUser.getUserName(),
                voStorageUser.getPersonalSignature(),
                voStorageUser.getEmail(),
                voStorageUser.getPhone());
    }

    public static Comment changeComment(VoCommentPage commentPage) {
        return new Comment(commentPage.getSortName(),
                commentPage.getSortOrder(),
                commentPage.getPageNumber(),
                commentPage.getPageSize(),
                commentPage.getTopicId());
    }
    public static CommentReply changeCommentReply(VoCommentReplyPage commentReplyPage) {
        return new CommentReply(commentReplyPage.getSortName(),
                commentReplyPage.getSortOrder(),
                commentReplyPage.getPageNumber(),
                commentReplyPage.getPageSize(),
                commentReplyPage.getCommentId());
    }

    public static Transaction changeTransaction(VoParams voParams) {
        DateUtils.DateRange dateTimeRange = DateUtils.findDateTimeRange(voParams.getTimeType(), voParams.getStartDateTime(), voParams.getEndDateTime());
        return new Transaction(voParams.getSortName(),
                voParams.getSortOrder(),
                voParams.getPageNumber(),
                voParams.getPageSize(),
                dateTimeRange.getStartDateTime(),
                dateTimeRange.getEndDateTime());
    }

    public static Transaction changeStorageTransaction(VoStorageTransaction storageTransaction) {
        return new Transaction(storageTransaction.getId(),
                storageTransaction.getTitle(),
                storageTransaction.getPrice(),
                storageTransaction.getDescribeContent(),
                storageTransaction.getContactMode(),
                storageTransaction.getContactPeople(),
                storageTransaction.getAddress());
    }

    public static RecordTime changeRecordTime(VoParams voParams) {
        DateUtils.DateRange dateTimeRange = DateUtils.findDateTimeRange(voParams.getTimeType(), voParams.getStartDateTime(), voParams.getEndDateTime());
        return new RecordTime(voParams.getSortName(),
                voParams.getSortOrder(),
                voParams.getPageNumber(),
                voParams.getPageSize(),
                dateTimeRange.getStartDateTime(),
                dateTimeRange.getEndDateTime());
    }

    public static RecordTime changeStorageRecordTime(VoStorageRecordTime storageRecordTime) {
        return new RecordTime(storageRecordTime.getId(),
                storageRecordTime.getTitle(),
                storageRecordTime.getDescribeContent());
    }
}
