package com.example.school.common.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/12 15:30
 * description:
 */
public class CacheKeys {

    /**
     * 用户登录次数计数
     */
    private static final String PREFIX_SHIRO_LOGIN_COUNT = "shiro:login_count_";

    /**
     * 用户登录是否被锁定
     */
    private static final String PREFIX_SHIRO_IS_LOCK = "shiro:is_lock_";

    /**
     * shiro 缓存
     */
    private static final String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * jwt验证token
     */
    private static final String PREFIX_JWT_ACCESS_TOKEN = "jwt:access_token:";

    /**
     * jwt刷新token
     */
    private static final String PREFIX_JWT_REFRESH_TOKEN = "jwt:refresh_token:";

    private static final String PREFIX_JPUSH_TOKEN = "jpush:";


    /**
     * 短信 验证码
     */
    private static final String PREFIX_SMS_CODE_PREFIX = "users:smscode:";

    /**
     * 邮件 验证码
     */
    private static final String PREFIX_EMAIL_PREFIX = "users:emailcode:";

    /**
     * 统计发送个数
     */
    private static final String PREFIX_VERIFICATION_CODE_NOTICE_COUNT = "verification_code:notice_count_";

    /**
     * 统计ip发送个数
     */
    private static final String PREFIX_VERIFICATION_CODE_IP_COUNT = "verification_code:ip_count_";

    /**
     * 获取锁定状态
     */
    private static final String PREFIX_VERIFICATION_CODE_NOTICE_IS_LOCK = "verification_code:phone_count_is_lock_";

    /**
     * 获取ip锁定状态
     */
    private static final String PREFIX_VERIFICATION_CODE_IP_IS_LOCK = "verification_code_ip:count_is_lock_";

    /**
     * 用户登录次数计数
     *
     * @param phone 手机号
     * @return key
     */
    public static String getShiroLoginCountKey(String phone) {
        return PREFIX_SHIRO_LOGIN_COUNT + phone;
    }

    /**
     * 用户登录是否被锁定
     *
     * @param phone 手机号
     * @return key
     */
    public static String getShiroIsLockKey(String phone) {
        return PREFIX_SHIRO_IS_LOCK + phone;
    }

    /**
     * jwt验证token
     *
     * @param deviceInfo 设备信息 {@link SysConst.DeviceInfo}
     * @param userId     用户id
     * @param ipLong     ip
     * @return key
     */
    public static String getJwtAccessTokenKey(String deviceInfo, Long userId, Long ipLong) {
        return PREFIX_JWT_ACCESS_TOKEN + deviceInfo + ":" + userId + ":" + ipLong;
    }

    /**
     * jwt刷新token
     *
     * @param deviceInfo 设备信息 {@link SysConst.DeviceInfo}
     * @param userId     用户id
     * @param ipLong     ip
     * @return key
     */
    public static String getJwtRefreshTokenKey(String deviceInfo, Long userId, Long ipLong) {
        return PREFIX_JWT_REFRESH_TOKEN + deviceInfo + ":" + userId + ":" + ipLong;
    }

    public static String getJpushTokenKey(Long userId) {
        return PREFIX_JPUSH_TOKEN + userId;
    }

    public static String getJpushTokenKey(Long userId, String registrationId) {
        return PREFIX_JPUSH_TOKEN + userId + ":" + registrationId;
    }

    /**
     * 短信 验证码
     *
     * @param verificationCodeType 验证码类型{@link SysConst.VerificationCodeType}
     * @param phone                手机号
     * @return key
     */
    public static String getSmsCodeKey(String verificationCodeType, String phone) {
        return PREFIX_SMS_CODE_PREFIX + verificationCodeType + ":" + phone;
    }

    /**
     * 邮箱 验证码
     *
     * @param verificationCodeType 验证码类型{@link SysConst.VerificationCodeType}
     * @param email                邮箱
     * @return key
     */
    public static String getEmailCodeKey(String verificationCodeType, String email) {
        return PREFIX_EMAIL_PREFIX + verificationCodeType + ":" + email;
    }

    /**
     * 统计发送个数
     *
     * @param notice 通知类型{@link SysConst.NoticeType}
     * @return key
     */
    public static String getVerificationCodeNoticeCountKey(String notice) {
        return PREFIX_VERIFICATION_CODE_NOTICE_COUNT + notice;
    }

    /**
     * 统计ip发送个数
     *
     * @param ip ip
     * @return key
     */
    public static String getVerificationCodeIpCountKey(String ip) {
        return PREFIX_VERIFICATION_CODE_IP_COUNT + ip;
    }

    /**
     * 获取锁定状态
     *
     * @param notice 通知类型{@link SysConst.NoticeType}
     * @return key
     */
    public static String getVerificationCodeNoticeIsLockKey(String notice) {
        return PREFIX_VERIFICATION_CODE_NOTICE_IS_LOCK + notice;
    }

    /**
     * 获取ip锁定状态
     *
     * @param ip ip
     * @return key
     */
    public static String getVerificationCodeIpIsLockKey(String ip) {
        return PREFIX_VERIFICATION_CODE_IP_IS_LOCK + ip;
    }

}
