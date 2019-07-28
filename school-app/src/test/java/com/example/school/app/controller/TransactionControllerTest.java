package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageTransaction;
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
public class TransactionControllerTest extends BaseAutoLoginTest {

    private Map<String, Object> params = Maps.newHashMap();
    private String url = "/app/transaction/";

    @Test
    public void saveTransaction() {
        url += "saveTransaction";
        VoStorageTransaction storageTransaction = new VoStorageTransaction();
        storageTransaction.setTitle("我发布的交易测试发的");
        storageTransaction.setPrice(10D);
        storageTransaction.setDescribeContent("我发布交易的描述");
        postJSONObject(url, storageTransaction);

    }

    @Test
    public void deleteTransaction() {
        url += "deleteTransaction";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findTransaction() {
        url += "findTransaction";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findTransactionEffective() {
        url += "findTransactionEffective";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(20);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findTransactionUser() {
        url += "findTransactionUser";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(10);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void enableTransactionZanOn() {
        url += "enableTransactionZanOn";
        params.put("id", 15);
        postParams(url, params);
    }

    @Test
    public void enableTransactionZanOff() {
        url += "enableTransactionZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void findTransactionComment() {
        url += "findTransactionComment";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void findTransactionCommentReply() {
        url += "findTransactionCommentReply";
        params.put("commentId", 1);
        postParams(url, params);
    }

    @Test
    public void findTransactionCommentAndReply() {
        url += "findTransactionCommentAndReply";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void saveTransactionComment() {
        url += "saveTransactionComment";
        params.put("topicId", 1);
        params.put("content", "我的评论内容");
        postParams(url, params);
    }

    @Test
    public void enableTransactionCommentZanOn() {
        url += "enableTransactionCommentZanOn";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableTransactionCommentZanOff() {
        url += "enableTransactionCommentZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void saveTransactionCommentReplyToComment() {
        url += "saveTransactionCommentReplyToComment";
        params.put("commentId", 1);
        params.put("content", "我的评论内容评论回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void saveTransactionCommentReplyToReply() {
        url += "saveTransactionCommentReplyToReply";
        params.put("commentId", 1);
        params.put("replyId", 1);
        params.put("content", "我的评论内容回复的回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }
}