package com.example.school.common.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/27 16:40
 * description:
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.verification")
public class VerificationProperties {
    private String hostYJLG;
    private String hostFZKJXY;
}
