package com.example.school.shiro.shiro.token;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:32
 * description:
 */
@Setter
@Getter
public class PasswordToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆类型
     */
    private String loginType;

    private String token;

    private String time;

    private String verificationCode;

    private String noticeType;

    public PasswordToken(String username, String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public PasswordToken(String username, String password, boolean rememberMe, String loginType) {
        super(username, password, rememberMe);
        this.loginType = loginType;
    }

    public PasswordToken(String username, String password, boolean rememberMe, String verificationCode, String noticeType, String loginType) {
        super(username, password, rememberMe);
        this.verificationCode = verificationCode;
        this.noticeType = noticeType;
        this.loginType = loginType;
    }


    public PasswordToken(String username, String password, String token, String time, String loginType) {
        super(username, password);
        this.loginType = loginType;
        this.token = token;
        this.time = time;
    }

}
