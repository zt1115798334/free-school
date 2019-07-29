package com.example.school.common.tools;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.example.school.common.constant.SysConst;
import com.example.school.common.constant.properties.ShortMessageProperties;
import com.example.school.common.exception.custom.OperationException;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/21 15:53
 * description: 短信工具类
 */
@AllArgsConstructor
@Slf4j
@Component
public class ShortMessageTool {

    private final ShortMessageProperties properties;

    private CommonResponse sendShortMessage(String phoneNumbers, String templateCode, String templateParam) throws ClientException {

//        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", properties.getDefaultConnectTimeout());
        System.setProperty("sun.net.client.defaultReadTimeout", properties.getDefaultReadTimeout());
//
//        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", properties.getAccessKeyId(), properties.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", properties.getProduct(), properties.getDomain());
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(properties.getDomain());
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.putQueryParameter("SignName", properties.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.putQueryParameter("TemplateCode", templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.putQueryParameter("TemplateParam", templateParam);

        //hint 此处可能会抛出异常，注意catch
        return acsClient.getCommonResponse(request);
    }

    public void sendShortMessageFromCode(String phoneNumbers, String code, String codeType) {
        CommonResponse response = null;
        try {
            String templateCode = null;
            if (Objects.equal(codeType, SysConst.VerificationCodeType.REGISTER.getType())) {
                templateCode = properties.getRegisterTemplateCode();
            }
            if (Objects.equal(codeType, SysConst.VerificationCodeType.LOGIN.getType())) {
                templateCode = properties.getLoginTemplateCode();
            }
            if (Objects.equal(codeType, SysConst.VerificationCodeType.FORGET.getType())) {
                templateCode = properties.getForgetTemplateCode();
            }
            JSONObject templateParam = new JSONObject();
            templateParam.put("code", code);
            response = sendShortMessage(phoneNumbers, templateCode, templateParam.toJSONString());
            log.info("data:{}", response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response == null || !Objects.equal(JSONObject.parseObject(response.getData()).getString("Code"), "OK")) {
            throw new OperationException("短信服务异常，请联系管理员");
        }

    }

}
