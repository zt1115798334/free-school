package com.example.school.common.externalService.verification;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/27 16:33
 * description:
 */
public interface VerificationService {

    /**
     * 验证学校--燕京理工
     *
     * @param username 用户名
     * @param password 密码
     */
    void verificationSchoolOfYJLG(String username, String password);
}
