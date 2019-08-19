package com.example.school.shiro.shiro.realm;

import com.example.school.common.constant.CacheKeys;
import com.example.school.common.constant.SysConst;
import com.example.school.common.constant.properties.AccountProperties;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.redis.StringRedisService;
import com.example.school.common.service.VerificationCodeService;
import com.example.school.common.utils.MD5Utils;
import com.example.school.common.utils.UserUtils;
import com.example.school.shiro.shiro.token.PasswordToken;
import com.google.common.base.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:37
 * description:
 */
@Slf4j
public class PasswordRealm extends AuthorizingRealm {

    @Setter
    private UserService userService;

    @Setter
    private AccountProperties accountProperties;

    @Setter
    private StringRedisService stringRedisService;

    @Setter
    private VerificationCodeService verificationCodeService;


    public Class<?> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }


    //这里只需要认证登录，成功之后派发 json web token 授权在那里进行
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordToken)) return null;
        PasswordToken token = (PasswordToken) authenticationToken;
        String loginType = token.getLoginType();
        String name = token.getUsername();
        String shiroLoginCountKey = CacheKeys.getShiroLoginCountKey(name);
        String shiroIsLockKey = CacheKeys.getShiroIsLockKey(name);

        if (Objects.equal(SysConst.LoginType.AJAX.getType(), loginType)) { //ajax登录
            String username = String.valueOf(token.getUsername());
            String password = String.valueOf(token.getPassword());

            Optional<String> shiroIsLock = stringRedisService.get(shiroIsLockKey);
            if (shiroIsLock.isPresent()) {
                if ("LOCK".equals(shiroIsLock.get())) {
                    throw new DisabledAccountException("由于密码输入错误次数大于5次，帐号已经禁止登录，请稍后重新尝试。");
                }
            }

            //密码进行加密处理  明文为  password+name
            Optional<User> userOptional = userService.findOptByAccount(name);
            if (!userOptional.isPresent()) {
                //登录错误开始计数
                increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException("账户不存在！");
            }
            User sysUser = userOptional.get();
            String userState = UserUtils.checkUserState(sysUser);
            if (StringUtils.isNotBlank(userState)) {
                //登录错误开始计数
                increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException(userState);
            }
            String pawDES = UserUtils.getEncryptPassword(username, password, sysUser.getSalt());
            // 从数据库获取对应用户名密码的用户
            String sysUserPassword = sysUser.getPassword();
            if (!Objects.equal(sysUserPassword, pawDES)) {
                //登录错误开始计数
                increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException("帐号或密码错误！");
            } else {
                //登录成功
                //更新登录时间 last login time
                userService.updateLastLoginTime(sysUser.getId());
                //清空登录计数
                stringRedisService.setNotContainExpire(shiroLoginCountKey, "0");
                stringRedisService.delete(shiroIsLockKey);
            }
            log.info("身份认证成功，登录用户：" + name);
            return new SimpleAuthenticationInfo(sysUser, password, getName());
        }
        if (Objects.equal(SysConst.LoginType.TOKEN.getType(), loginType)) {    //token登陆
            String apiToken = token.getToken();
            String apiTime = token.getTime();
            Optional<User> userOptional = userService.findOptByAccount(name);
            if (!userOptional.isPresent()) {
                throw new AccountException("账户不存在！");
            }
            User sysUser = userOptional.get();
            String userState = UserUtils.checkUserState(sysUser);
            if (StringUtils.isNotBlank(userState)) {
                throw new AccountException(userState);
            }
            String password = sysUser.getPassword();
            String mToken = MD5Utils.MD5(name + password + apiTime);
            if (Objects.equal(apiToken, mToken)) {
                log.info("身份认证成功，登录用户：" + name);
                return new SimpleAuthenticationInfo(sysUser, "888888", getName());
            } else {
                throw new AccountException("token值不正确！");
            }
        }
        if (Objects.equal(SysConst.LoginType.VERIFICATION_CODE.getType(), loginType)) { //验证码登录
            String noticeType = token.getNoticeType();

            Optional<String> shiroIsLock = stringRedisService.get(shiroIsLockKey);
            if (shiroIsLock.isPresent()) {
                if ("LOCK".equals(shiroIsLock.get())) {
                    throw new DisabledAccountException("你正在尝试非法登录，帐号已经禁止登录，请稍后重新尝试。");
                }
            }

            Optional<User> userOptional = Optional.empty();
            if (Objects.equal(noticeType, SysConst.NoticeType.PHONE.getType())) {
                userOptional = userService.findOptByPhone(name);
            }
            if (!userOptional.isPresent()) {
                increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException("手机号不存在！");
            }
            User sysUser = userOptional.get();
            String userState = UserUtils.checkUserState(userOptional.get());
            if (StringUtils.isNotBlank(userState)) {
                increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException(userState);
            }
            String verificationCode = token.getVerificationCode();
            boolean state = verificationCodeService.validateCode(name, noticeType, verificationCode, SysConst.VerificationCodeType.LOGIN.getType());
            if (state) {
                //登录成功
                //更新登录时间 last login time
                userService.updateLastLoginTime(sysUser.getId());
                //清空登录计数
                stringRedisService.setNotContainExpire(shiroLoginCountKey, "0");
                stringRedisService.delete(shiroIsLockKey);
                verificationCodeService.deleteCode(name, noticeType, verificationCode, SysConst.VerificationCodeType.LOGIN.getType());
                log.info("身份认证成功，登录用户：" + name);
                return new SimpleAuthenticationInfo(sysUser, "888888", getName());
            } else {
                //登录错误开始计数
                increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException("验证码值不正确！");
            }
        }
        throw new AccountException("非法登陆！");
    }

    private void increment(String shiroLoginCountKey, String shiroIsLockKey) {
        //访问一次，计数一次
        stringRedisService.increment(shiroLoginCountKey, 1);
        //计数大于5时，设置用户被锁定10分钟
        stringRedisService.get(shiroLoginCountKey).ifPresent(shiroLoginCount -> {
            Integer firstErrorAccountErrorCount = accountProperties.getFirstErrorAccountErrorCount();
            Integer secondErrorAccountErrorCount = accountProperties.getSecondErrorAccountErrorCount();
            Integer thirdErrorAccountErrorCount = accountProperties.getThirdErrorAccountErrorCount();
            int parseInt = Integer.parseInt(shiroLoginCount);

            if (parseInt == firstErrorAccountErrorCount) {
                stringRedisService.expire(shiroIsLockKey, "LOCK", accountProperties.getFirstErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (parseInt == secondErrorAccountErrorCount) {
                stringRedisService.expire(shiroIsLockKey, "LOCK", accountProperties.getSecondErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (parseInt >= thirdErrorAccountErrorCount) {
                stringRedisService.expire(shiroIsLockKey, "LOCK", accountProperties.getThirdErrorAccountLockTime(), TimeUnit.HOURS);
            }

        });
    }
}
