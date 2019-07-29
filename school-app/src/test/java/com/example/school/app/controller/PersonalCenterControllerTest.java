package com.example.school.app.controller;

import com.example.school.app.BaseAutoLoginTest;
import com.example.school.common.base.entity.vo.VoStorageUser;
import com.google.common.collect.Maps;
import org.junit.Test;

import static org.junit.Assert.*;

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
}