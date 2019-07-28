package com.example.school.shiro.shiro.realm;

import com.example.school.common.constant.CacheKeys;
import com.example.school.common.constant.SysConst;
import com.example.school.common.constant.SystemStatusCode;
import com.example.school.common.redis.StringRedisService;
import com.example.school.common.utils.JwtUtils;
import com.example.school.common.utils.NetworkUtil;
import com.example.school.common.utils.UserUtils;
import com.example.school.shiro.shiro.token.JwtToken;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.service.UserService;
import com.google.common.collect.Sets;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:41
 * description: 进行授权 以及 jwt的验证
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Setter
    private JwtUtils jwtUtils;
    @Setter
    private UserService userService;
    @Setter
    private StringRedisService stringRedisService;

    public Class<?> getAuthenticationTokenClass() {
        // 此realm只支持jwtToken
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        log.info("开始执行权限认证...");
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> permissionSet = Sets.newHashSet();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user != null) {
            if (UserUtils.checkUserFrozen(user)) { //冻结
                permissionSet.add(SystemStatusCode.USER_FROZEN.getName());
            } else if (Objects.equals(user.getDeleteState(), SysConst.DeleteState.DELETE.getCode())) {
                permissionSet.add(SystemStatusCode.USER_DELETE.getName());  //未找到
            } else {
                permissionSet.add(SystemStatusCode.USER_NORMAL.getName()); //用户正常
            }

        } else {
            permissionSet.add(SystemStatusCode.USER_NOT_FOUND.getName());  //未找到
        }
        info.setStringPermissions(permissionSet);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        开始验证token值的正确...
        if (!(authenticationToken instanceof JwtToken)) return null;
        JwtToken jwtToken = (JwtToken) authenticationToken;

        String token = (String) jwtToken.getCredentials();
        Long userId = jwtToken.getUserId();
        String ip = jwtToken.getIpHost();
        Long ipLong = NetworkUtil.ipToLong(ip);
        if (StringUtils.isNotBlank(token)) {
            if (userId != null) {
                Optional<String> accessTokenRedis = stringRedisService.get(CacheKeys.getJwtAccessTokenKey(jwtToken.getDeviceInfo(), userId, ipLong));
                if (accessTokenRedis.isPresent()) {
                    Optional<User> userOptional = userService.findByIdNotDelete(userId);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        if (jwtUtils.validateToken(token, user)) {
                            return new SimpleAuthenticationInfo(user, token, getName());
                        } else {
                            throw new AuthenticationException(SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName());
                        }
                    } else {
                        throw new AuthenticationException(SystemStatusCode.USER_NOT_FOUND.getName());
                    }
                } else {
                    throw new AuthenticationException(SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName());
                }

            } else {
                throw new AuthenticationException(SystemStatusCode.JWT_NOT_FOUND.getName());
            }
        }
        throw new AuthenticationException(SystemStatusCode.JWT_NOT_FOUND.getName());
    }
}
