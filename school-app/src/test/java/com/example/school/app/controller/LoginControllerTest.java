package com.example.school.app.controller;

import com.example.school.app.BaseTest;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 10:39
 * description:
 */
public class LoginControllerTest extends BaseTest {
    private Map<String, Object> params = Maps.newHashMap();
    private String url = "/app/login/";

    @Test
    public void login() {
        url += "login";
        params.put("username", "15600663638");
        params.put("password", "15600663638");
        params.put("registrationId", "171976fa8aa41ef7049");
        postParams(url, params);
    }

    @Test
    public void verificationCodeLogin() {
    }

    @Test
    public void sendVerificationCodeByLogin() {
    }

    @Test
    public void logout() {
    }

    @Test
    public void validatePhoneByRegister() {
        url += "validatePhoneByRegister";
        params.put("phone", "15600663638");
        postParams(url, params);
    }

    @Test
    public void sendPhoneCodeByRegister() {
        url += "sendPhoneCodeByRegister";
        params.put("phone", "15830088978");
        postParams(url, params);
    }

    @Test
    public void register() {
        url += "register";
        params.put("phone", "13699210234");
        params.put("code", "145087");
        params.put("password", "123456");
        postParams(url, params);
    }

    @Test
    public void validatePhoneCodeByForget() {
        url += "validatePhoneCodeByForget";
        params.put("phone", "15130097582");
        postParams(url, params);
    }

    @Test
    public void sendPhoneCodeByForget() {
        url += "sendPhoneCodeByForget";
        params.put("phone", "15600663638");
        postParams(url, params);
    }

    @Test
    public void modifyPassword() {
        url += "modifyPassword";
        params.put("phone", "15600663638");
        params.put("code", "888743");
        params.put("password", "15600663638");
        postParams(url, params);
    }

    @Test
    public void validateSchoolAdministration() {
        url += "validateSchoolAdministration";
        params.put("schoolCode", "2");
        params.put("studentId", "19013122");
        params.put("studentPwd", "Wy17637987113.");
        postParams(url, params);
    }
}