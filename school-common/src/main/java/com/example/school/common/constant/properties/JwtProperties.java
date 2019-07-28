package com.example.school.common.constant.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/3 14:21
 * description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {

    private String header;
    private Long expiration;
    private Long refreshExpiration;
    private Long rememberMeExpiration;
    private Long rememberMeRefreshExpiration;
    private String tokenHead;
    private String secret;

}
