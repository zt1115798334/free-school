package com.example.school.shiro.shiro.service.impl;

import com.example.school.common.constant.CacheKeys;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.entity.UserRegistration;
import com.example.school.common.mysql.service.UserRegistrationService;
import com.example.school.common.redis.StringRedisService;
import com.example.school.common.utils.JwtUtils;
import com.example.school.common.utils.NetworkUtil;
import com.example.school.shiro.shiro.service.CommonLoginService;
import com.example.school.shiro.shiro.token.PasswordToken;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/1/17 17:49
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommonLoginServiceImpl implements CommonLoginService {

    private final JwtUtils jwtUtils;

    private final StringRedisService stringRedisService;

    private final UserRegistrationService userRegistrationService;

    @Override
    public String login(PasswordToken token, String ip, String deviceInfo) throws OperationException {
        return login(token, false, ip, deviceInfo, null);
    }

    @Override
    public String login(PasswordToken token, Boolean rememberMe, String ip, String deviceInfo) throws OperationException {
        return login(token, rememberMe, ip, deviceInfo, null);
    }

    @Override
    public String login(PasswordToken token, String ip, String deviceInfo, String registrationId) throws OperationException {
        return login(token, false, ip, deviceInfo, registrationId);
    }

    @Override
    public String login(PasswordToken token, Boolean rememberMe, String ip, String deviceInfo, String registrationId) throws OperationException {
        Long ipLong = Objects.equal(deviceInfo, SysConst.DeviceInfo.WEB.getType()) ?
                NetworkUtil.ipToLong(ip) : 0L;
        SecurityUtils.getSubject().login(token);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Long userId = user.getId();
        String accessToken = jwtUtils.generateAccessToken(user, rememberMe);
        String refreshToken = jwtUtils.generateRefreshToken(user, rememberMe);

        //token 存储redis
        stringRedisService.saveAccessToken(CacheKeys.getJwtAccessTokenKey(deviceInfo, userId, ipLong), accessToken, rememberMe);
        stringRedisService.saveRefreshToken(CacheKeys.getJwtRefreshTokenKey(deviceInfo, userId, ipLong), refreshToken, rememberMe);
        if (StringUtils.isNotBlank(registrationId)) {
            stringRedisService.deleteByLike(CacheKeys.getJpushTokenKey(userId));
            stringRedisService.saveRefreshToken(CacheKeys.getJpushTokenKey(userId, registrationId), registrationId, rememberMe);
        }
        if (StringUtils.equals(deviceInfo, SysConst.DeviceInfo.MOBILE.getType())) {
            UserRegistration userRegistration = new UserRegistration(userId, registrationId, accessToken);
            userRegistrationService.save(userRegistration);
        }

        return accessToken;
    }

    @Override
    public void logout(Long currentUserId, String ip, String deviceInfo) {
        this.logout(currentUserId, ip, deviceInfo, StringUtils.EMPTY);
    }

    @Override
    public void logout(Long currentUserId, String ip, String deviceInfo, String registrationId) {
        Long ipLong = Objects.equal(deviceInfo, SysConst.DeviceInfo.WEB.getType()) ?
                NetworkUtil.ipToLong(ip) : 0L;
        stringRedisService.delete(CacheKeys.getJwtAccessTokenKey(deviceInfo, currentUserId, ipLong));
        stringRedisService.delete(CacheKeys.getJwtRefreshTokenKey(deviceInfo, currentUserId, ipLong));
        if (StringUtils.isNotEmpty(registrationId)) {
            stringRedisService.delete(CacheKeys.getJpushTokenKey(currentUserId, registrationId));
        }
        SecurityUtils.getSubject().logout();
    }
}
