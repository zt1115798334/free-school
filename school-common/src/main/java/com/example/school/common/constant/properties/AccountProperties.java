package com.example.school.common.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 1/7/2019 9:58 AM
 * description: 用户配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.account")
public class AccountProperties {

    private Integer firstErrorAccountLockTime;

    private Integer firstErrorAccountErrorCount;

    private Integer secondErrorAccountLockTime;

    private Integer secondErrorAccountErrorCount;

    private Integer thirdErrorAccountLockTime;

    private Integer thirdErrorAccountErrorCount;

}
