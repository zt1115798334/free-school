package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageRecordTime;
import com.example.school.common.constant.SysConst;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/20 9:44
 * description:
 */
public class RecordTimeControllerTest extends BaseAutoLoginTest {

    private Map<String, Object> params = Maps.newHashMap();
    private String url = "/app/recordTime/";

    @Test
    public void saveRecordTime() {
        url += "saveRecordTime";
        VoStorageRecordTime storageRecordTime = new VoStorageRecordTime();
        storageRecordTime.setTitle("我发布的时光测试发的");
        storageRecordTime.setDescribeContent("我发布时光的描述");
        postJSONObject(url, storageRecordTime);

    }

    @Test
    public void deleteRecordTime() {
        url += "deleteRecordTime";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findRecordTime() {
        url += "findRecordTime";
        params.put("id", 31L);
        postParams(url, params);
    }

    @Test
    public void findRecordTimeEffective() {
        url += "findRecordTimeEffective";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(20);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findRecordTimeUser() {
        url += "findRecordTimeUser";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(10);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void enableRecordTimeZanOn() {
        url += "enableRecordTimeZanOn";
        params.put("id", 15);
        postParams(url, params);
    }

    @Test
    public void enableRecordTimeZanOff() {
        url += "enableRecordTimeZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void findRecordTimeComment() {
        url += "findRecordTimeComment";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void findRecordTimeCommentReply() {
        url += "findRecordTimeCommentReply";
        params.put("commentId", 1);
        postParams(url, params);
    }

    @Test
    public void findRecordTimeCommentAndReply() {
        url += "findRecordTimeCommentAndReply";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(31L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void saveRecordTimeComment() {
        url += "saveRecordTimeComment";
        params.put("topicId", 1);
        params.put("content", "我的评论内容");
        postParams(url, params);
    }

    @Test
    public void enableRecordTimeCommentZanOn() {
        url += "enableRecordTimeCommentZanOn";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableRecordTimeCommentZanOff() {
        url += "enableRecordTimeCommentZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void saveRecordTimeCommentReplyToComment() {
        url += "saveRecordTimeCommentReplyToComment";
        params.put("commentId", 1);
        params.put("content", "我的评论内容评论回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void saveRecordTimeCommentReplyToReply() {
        url += "saveRecordTimeCommentReplyToReply";
        params.put("commentId", 1);
        params.put("replyId", 1);
        params.put("content", "我的评论内容回复的回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }
}