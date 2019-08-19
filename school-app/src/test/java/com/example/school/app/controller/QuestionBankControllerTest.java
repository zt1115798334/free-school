package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageQuestionBank;
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
public class QuestionBankControllerTest extends BaseAutoLoginTest {

    private Map<String, Object> params = Maps.newHashMap();
    private String url = "/app/questionBank/";

    @Test
    public void saveQuestionBank() {
        url += "saveQuestionBank";
        VoStorageQuestionBank storageQuestionBank = new VoStorageQuestionBank();
        storageQuestionBank.setTitle("我发布的题库测试发的");
        storageQuestionBank.setDescribeContent("我发布题库的描述");
        storageQuestionBank.setIntegral(10L);
        postJSONObject(url, storageQuestionBank);

    }

    @Test
    public void deleteQuestionBank() {
        url += "deleteQuestionBank";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findQuestionBank() {
        url += "findQuestionBank";
        params.put("id", 13L);
        postParams(url, params);
    }

    @Test
    public void findQuestionBankEffective() {
        url += "findQuestionBankEffective";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(20);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findQuestionBankUser() {
        url += "findQuestionBankUser";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(10);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findQuestionBankCollection() {
        url += "findQuestionBankCollection";
        VoPage voPage = new VoPage();
        voPage.setPageNumber(1);
        voPage.setPageSize(10);
        postJSONObject(url, params);
    }

    @Test
    public void enableQuestionBankZanOn() {
        url += "enableQuestionBankZanOn";
        params.put("id", 15);
        postParams(url, params);
    }

    @Test
    public void enableQuestionBankZanOff() {
        url += "enableQuestionBankZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableQuestionBankCollectionOn() {
        url += "enableQuestionBankCollectionOn";
        params.put("id", 1);
        postParams(url, params);
    }


    @Test
    public void enableQuestionBankCollectionOff() {
        url += "enableQuestionBankCollectionOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void findQuestionBankComment() {
        url += "findQuestionBankComment";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void findQuestionBankCommentReply() {
        url += "findQuestionBankCommentReply";
        params.put("commentId", 1);
        postParams(url, params);
    }

    @Test
    public void findQuestionBankCommentAndReply() {
        url += "findQuestionBankCommentAndReply";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void saveQuestionBankComment() {
        url += "saveQuestionBankComment";
        params.put("topicId", 1);
        params.put("content", "我的评论内容");
        postParams(url, params);
    }

    @Test
    public void enableQuestionBankCommentZanOn() {
        url += "enableQuestionBankCommentZanOn";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableQuestionBankCommentZanOff() {
        url += "enableQuestionBankCommentZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void saveQuestionBankCommentReplyToComment() {
        url += "saveQuestionBankCommentReplyToComment";
        params.put("commentId", 1);
        params.put("content", "我的评论内容评论回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void saveQuestionBankCommentReplyToReply() {
        url += "saveQuestionBankCommentReplyToReply";
        params.put("commentId", 1);
        params.put("replyId", 1);
        params.put("content", "我的评论内容回复的回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }
}