package com.example.school.common.externalService.verification.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.school.common.constant.properties.VerificationProperties;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.externalService.verification.VerificationService;
import com.example.school.common.utils.HttpClientUtils;
import com.example.school.common.utils.MStringUtils;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/27 16:34
 * description:
 */
@AllArgsConstructor
@Component
public class VerificationServiceImpl implements VerificationService {


    private final VerificationProperties verificationProperties;

    @Override
    public void verificationSchoolOfYJLG(String username, String password) {
        this.verificationSchool(verificationProperties.getHostYJLG(), username, password);
    }

    @Override
    public void verificationSchoolOfFZKJXY(String username, String password) {
        this.verificationSchool(verificationProperties.getHostFZKJXY(), username, password);
    }

    private void verificationSchool(String url, String username, String password) {
        JSONObject params = new JSONObject();
        params.put("username", username);
        params.put("password", password);
        String str = HttpClientUtils.getInstance().httpPostFrom(url, Collections.emptyMap(), params.getInnerMap());
        JSONObject json = JSON.parseObject(str);
        if (Objects.equal(json.getInteger("status"), HttpStatus.SC_BAD_REQUEST)) {
            String data = MStringUtils.unicodeDecode(json.getString("data"));
            if (Objects.equal("验证码识别错误", data)) {
                throw new OperationException("验证码识别错误,请从新验证");
            }
            if (Objects.equal("缺少参数", data)) {
                throw new OperationException("缺少参数,请联系管理员");
            }
            throw new OperationException(data);
        }
    }
}
