package com.example.school.common.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/30 10:09
 * description: 极光推送配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.jpush")
public class JPushProperties {

    private String appKey;

    private String masterSecret;

    private String liveTime;

}
