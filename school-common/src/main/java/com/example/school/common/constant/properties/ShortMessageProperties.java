package com.example.school.common.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/21 15:11
 * description: 短信配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.short-message")
public class ShortMessageProperties {

    private String product;
    private String domain;
    private String accessKeyId;
    private String accessKeySecret;
    private String defaultConnectTimeout;
    private String defaultReadTimeout;
    private String signName;
    private String loginTemplateCode;
    private String registerTemplateCode;
    private String forgetTemplateCode;
    private String outId;
}
