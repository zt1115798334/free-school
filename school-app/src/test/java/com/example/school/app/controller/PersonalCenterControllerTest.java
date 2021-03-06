package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoStorageUser;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/7/7 10:13
 * description:
 */
public class PersonalCenterControllerTest extends BaseAutoLoginTest {

    private String url = "/app/personalCenter/";

    @Test
    public void findUserInfo() {
        url += "findUserInfo";
        postParams(url, Maps.newHashMap());

    }

    @Test
    public void saveUserInfo() {
        url += "saveUserInfo";
        VoStorageUser voStorageUser = new VoStorageUser();
        voStorageUser.setUserName("2222");
        voStorageUser.setPhone("15130097582");
        voStorageUser.setEmail("zhang@qq.com");
        postJSONObject(url, voStorageUser);
    }

    @Test
    public void saveSchoolAdministration() {
        url += "saveSchoolAdministration";
        Map<String, Object> params = Maps.newHashMap();
        params.put("studentId", "160740061");
        params.put("studentPwd", "lixin123");
        postParams(url, params);
    }

    @Test
    public void findSchoolAdministration() {
        url += "findSchoolAdministration";
        postParams(url, Maps.newHashMap());
    }

    @Test
    public void findSchoolTimetable() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("semester", "2018-2019-2");
        params.put("weeklyTimes", "1");
        url += "findSchoolTimetable";
        postParams(url, params);
    }

    @Test
    public void relieveSchoolAdministration() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("phone", "15600663638");
        url += "relieveSchoolAdministration";
        postParams(url, params);
    }
}