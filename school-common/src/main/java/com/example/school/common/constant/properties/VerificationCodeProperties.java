package com.example.school.common.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 1/7/2019 9:56 AM
 * description: 验证码配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.verification-code")
public class VerificationCodeProperties {

    private Integer codeLen;

    private Integer codeExpires;

    private Integer codeRequestingPartyLockTime;

    private Integer codeRequestingPartyMaxCount;
}
