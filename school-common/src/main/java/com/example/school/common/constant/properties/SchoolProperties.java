package com.example.school.common.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/3 19:41
 * description:
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.school")
public class SchoolProperties {
    private String host;
    private RSA rsa = new RSA();

    @Getter
    @Setter
    public static class RSA {
        private String privateKey;
        private String publicKey;
    }
}
