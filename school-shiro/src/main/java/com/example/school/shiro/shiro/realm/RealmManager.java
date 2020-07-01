package com.example.school.shiro.shiro.realm;

import com.example.school.common.constant.properties.AccountProperties;
import com.example.school.common.mysql.service.PermissionService;
import com.example.school.common.redis.StringRedisService;
import com.example.school.common.service.VerificationCodeService;
import com.example.school.common.utils.JwtUtils;
import com.example.school.shiro.shiro.token.JwtToken;
import com.example.school.common.mysql.service.UserService;
import com.example.school.shiro.shiro.token.PasswordToken;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:26
 * description: realm管理器
 */
@AllArgsConstructor
@Component
public class RealmManager {

    private final UserService userService;

    private final AccountProperties accountProperties;

    private final StringRedisService stringRedisService;

    private final VerificationCodeService verificationCodeService;

    private final JwtUtils jwtUtils;

    private final PermissionService permissionService;

    public List<Realm> initGetRealm() {
        List<Realm> realmList = Lists.newArrayList();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setUserService(userService);
        passwordRealm.setAccountProperties(accountProperties);
        passwordRealm.setStringRedisService(stringRedisService);
        passwordRealm.setVerificationCodeService(verificationCodeService);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setUserService(userService);
        jwtRealm.setJwtUtils(jwtUtils);
        jwtRealm.setStringRedisService(stringRedisService);
        jwtRealm.setPermissionService(permissionService);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);

        return Collections.unmodifiableList(realmList);
    }
}
