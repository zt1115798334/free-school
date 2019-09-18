package com.example.school.shiro.shiro.realm;

import com.example.school.common.constant.CacheKeys;
import com.example.school.common.constant.SysConst;
import com.example.school.common.constant.SystemStatusCode;
import com.example.school.common.mysql.entity.Permission;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.service.PermissionService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.redis.StringRedisService;
import com.example.school.common.utils.JwtUtils;
import com.example.school.common.utils.NetworkUtil;
import com.example.school.common.utils.UserUtils;
import com.example.school.shiro.shiro.token.JwtToken;
import com.google.common.base.Objects;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Setter
    private PermissionService permissionService;

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
            } else if (Objects.equal(user.getDeleteState(), SysConst.DeleteState.DELETE.getCode())) {
                permissionSet.add(SystemStatusCode.USER_DELETE.getName());  //未找到
            } else {
                permissionSet.add(SystemStatusCode.USER_NORMAL.getName()); //用户正常
                //添加系统权限
                List<Permission> permissionList = permissionService.findByUserId(user.getId());
                Set<String> collect = permissionList.stream()
                        .map(Permission::getPermission)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet());
                permissionSet.addAll(collect);
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
        String deviceInfo = jwtToken.getDeviceInfo();
        Long userId = jwtToken.getUserId();
        String ip = jwtToken.getIpHost();
        Long ipLong = Objects.equal(deviceInfo, SysConst.DeviceInfo.WEB.getType()) ?
                NetworkUtil.ipToLong(ip) : 0L;
        if (StringUtils.isNotBlank(token)) {
            if (userId != null) {
                Optional<String> accessTokenRedis = stringRedisService.get(CacheKeys.getJwtAccessTokenKey(deviceInfo, userId, ipLong));
                if (accessTokenRedis.isPresent()) {
                    if (Objects.equal(accessTokenRedis.get(), token)) {
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

                        throw new AuthenticationException(SystemStatusCode.JWT_DIFFERENT_PLACES.getName());
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
