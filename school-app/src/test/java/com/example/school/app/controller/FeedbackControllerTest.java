package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoStorageFeedback;
import com.example.school.common.constant.SysConst;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/7/12 16:55
 * description:
 */
public class FeedbackControllerTest extends BaseAutoLoginTest {

    private String url = "/app/feedback/";
    private Map<String, Object> params = Maps.newHashMap();


    @Test
    public void saveFeedback() {
        url += "saveFeedback";
        VoStorageFeedback storageFeedback = new VoStorageFeedback();
        storageFeedback.setFeedbackType(SysConst.FeedbackType.FEEDBACK_TYPE_CUSTOM.getCode());
        storageFeedback.setContactMode("15130097582");
        storageFeedback.setContent("test feedback");
        postJSONObject(url, storageFeedback);
    }

    @Test
    public void saveFeedbackImg() {

    }
}