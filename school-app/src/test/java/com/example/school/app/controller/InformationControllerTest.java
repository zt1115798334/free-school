package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoCommentPage;
import com.example.school.common.base.entity.vo.VoPage;
import com.example.school.common.base.entity.vo.VoParams;
import com.example.school.common.base.entity.vo.VoStorageInformation;
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
public class InformationControllerTest extends BaseAutoLoginTest {

    private Map<String, Object> params = Maps.newHashMap();
    private String url = "/app/information/";

    @Test
    public void saveInformation() {
        url += "saveInformation";
        VoStorageInformation storageInformation = new VoStorageInformation();
        storageInformation.setTitle("我发布的资讯测试发的");
        storageInformation.setDescribeContent("我发布资讯的描述");
        postJSONObject(url, storageInformation);

    }

    @Test
    public void deleteInformation() {
        url += "deleteInformation";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findInformation() {
        url += "findRoInformation";
        params.put("id", 11L);
        postParams(url, params);
    }

    @Test
    public void findInformationEffective() {
        url += "findInformationEffective";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(20);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findInformationUser() {
        url += "findInformationUser";
        VoParams params = new VoParams();
        params.setPageNumber(1);
        params.setPageSize(10);
        params.setTimeType(SysConst.TimeType.WEEK.getType());
        postJSONObject(url, params);
    }

    @Test
    public void findInformationCollection() {
        url += "findInformationCollection";
        VoPage voPage = new VoPage();
        voPage.setPageNumber(1);
        voPage.setPageSize(10);
        postJSONObject(url, params);
    }

    @Test
    public void enableInformationZanOn() {
        url += "enableInformationZanOn";
        params.put("id", 15);
        postParams(url, params);
    }

    @Test
    public void enableInformationZanOff() {
        url += "enableInformationZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableInformationCollectionOn() {
        url += "enableInformationCollectionOn";
        params.put("id", 1);
        postParams(url, params);
    }


    @Test
    public void enableInformationCollectionOff() {
        url += "enableInformationCollectionOff";
        params.put("id", 1);
        postParams(url, params);
    }
    @Test
    public void findInformationComment() {
        url += "findInformationComment";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void findInformationCommentReply() {
        url += "findInformationCommentReply";
        params.put("commentId", 1);
        postParams(url, params);
    }

    @Test
    public void findInformationCommentAndReply() {
        url += "findInformationCommentAndReply";
        VoCommentPage voCommentPage = new VoCommentPage();
        voCommentPage.setTopicId(1L);
        voCommentPage.setPageNumber(1);
        voCommentPage.setPageSize(10);
        postJSONObject(url, voCommentPage);
    }

    @Test
    public void saveInformationComment() {
        url += "saveInformationComment";
        params.put("topicId", 1);
        params.put("content", "我的评论内容");
        postParams(url, params);
    }

    @Test
    public void enableInformationCommentZanOn() {
        url += "enableInformationCommentZanOn";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void enableInformationCommentZanOff() {
        url += "enableInformationCommentZanOff";
        params.put("id", 1);
        postParams(url, params);
    }

    @Test
    public void saveInformationCommentReplyToComment() {
        url += "saveInformationCommentReplyToComment";
        params.put("commentId", 1);
        params.put("content", "我的评论内容评论回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }

    @Test
    public void saveInformationCommentReplyToReply() {
        url += "saveInformationCommentReplyToReply";
        params.put("commentId", 1);
        params.put("replyId", 1);
        params.put("content", "我的评论内容回复的回复");
        params.put("fromUserId", 1);
        postParams(url, params);
    }
}