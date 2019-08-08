package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageKnowing;
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
public class KnowingControllerTest extends BaseAutoLoginTest {

    private Map<String, Object> params = Maps.newHashMap();
    private String url = "/app/knowing/";

    @Test
    public void saveKnowing() {
        url += "saveKnowing";
        VoStorageKnowing storageKnowing = new VoStorageKnowing();
        storageKnowing.setTitle("我发布的问答测试发的");
        storageKnowing.setDescribeContent("我发布问答的描述");
        storageKnowing.setIntegral(20L);
        postJSONObject(url, storageKnowing);

    }

    @Test
    public void deleteKnowing() {
        url += "deleteKnowing";
        params.put("id", 1L);
        postParams(url, params);
    }

    @Test
    public void findKnowing() {
        url += "findKnowing";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findKnowingEffective() {
        url += "findKnowingEffective";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(20);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findKnowingUser() {
        url += "findKnowingUser";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(10);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findKnowingCollection() {
        url += "findKnowingCollection";
        VoPage voPage = new VoPage();
        voPage.setPageNumber(1);
        voPage.setPageSize(10);
        postJSONObject(url, params);
    }

    @Test
    public void enableKnowingZanOn() {
        url += "enableKnowingZanOn";
        params.put("id", 15);
        postParams(url, params);
    }

    @Test
    public void enableKnowingZanOff() {
        url += "enableKnowingZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableKnowingCollectionOn() {
        url += "enableKnowingCollectionOn";
        params.put("id", 1);
        postParams(url, params);
    }


    @Test
    public void enableKnowingCollectionOff() {
        url += "enableKnowingCollectionOff";
        params.put("id", 1);
        postParams(url, params);
    }
    @Test
    public void findKnowingComment() {
        url += "findKnowingComment";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(9L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void findKnowingCommentReply() {
        url += "findKnowingCommentReply";
        params.put("commentId", 1);
        postParams(url, params);
    }

    @Test
    public void findKnowingCommentAndReply() {
        url += "findKnowingCommentAndReply";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(12L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void saveKnowingComment() {
        url += "saveKnowingComment";
        params.put("topicId", 2);
        params.put("content", "我的评论内容");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void enableKnowingCommentZanOn() {
        url += "enableKnowingCommentZanOn";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableKnowingCommentZanOff() {
        url += "enableKnowingCommentZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void saveKnowingCommentReplyToComment() {
        url += "saveKnowingCommentReplyToComment";
        params.put("commentId", 1);
        params.put("content", "我的评论内容评论回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void saveKnowingCommentReplyToReply() {
        url += "saveKnowingCommentReplyToReply";
        params.put("commentId", 1);
        params.put("replyId", 1);
        params.put("content", "我的评论内容回复的回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void adoptKnowingComment() {
        url += "adoptKnowingComment";
        params.put("topicId", 2);
        params.put("commentId", 2);
        params.put("commentUserId", 2);
        postParams(url, params);
    }
}