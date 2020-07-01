package com.example.school.common.service.impl;

import com.example.school.common.constant.CacheKeys;
import com.example.school.common.constant.SysConst;
import com.example.school.common.constant.properties.VerificationCodeProperties;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.service.VerificationCodeLog;
import com.example.school.common.redis.StringRedisService;
import com.example.school.common.service.VerificationCodeService;
import com.example.school.common.tools.ShortMessageTool;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.NetworkUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/28 14:30
 * description:
 */
@AllArgsConstructor
@Component
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeProperties verificationCodeProperties;

    private final ShortMessageTool shortMessageTool;

    private final StringRedisService stringRedisService;

    private final VerificationCodeLog verificationCodeLogService;

    @Override
    public void sendCode(String ip, String noticeContent, String noticeType, String codeType) {
        Long ipLong = NetworkUtil.ipToLong(ip);
        String ipCountKey = CacheKeys.getVerificationCodeIpCountKey(String.valueOf(ipLong));
        String noticeContentCountKey = CacheKeys.getVerificationCodeNoticeCountKey(noticeContent);

        String ipIsLockKey = CacheKeys.getVerificationCodeIpIsLockKey(String.valueOf(ipLong));
        String noticeContentIsLockKey = CacheKeys.getVerificationCodeNoticeIsLockKey(noticeContent);

        Integer requestingPartyMaxCount = verificationCodeProperties.getCodeRequestingPartyMaxCount();
        Integer requestingPartyLockTime = verificationCodeProperties.getCodeRequestingPartyLockTime();

        //锁定ip
        contentLock(ipCountKey, ipIsLockKey, requestingPartyMaxCount, requestingPartyLockTime);
        //锁定邮件或者手机号
        contentLock(noticeContentCountKey, noticeContentIsLockKey, requestingPartyMaxCount, requestingPartyLockTime);

        Optional<String> ipIsLockOptional = stringRedisService.get(ipIsLockKey);
        Optional<String> noticeContentIsLockOptional = stringRedisService.get(noticeContentIsLockKey);
        if (ipIsLockOptional.isPresent()) {
            if (Objects.equals("LOCK", ipIsLockOptional.get())) {
                throw new OperationException("已超过最大数量，请于24小时后一会重新尝试");
            }
        }
        if (noticeContentIsLockOptional.isPresent()) {
            if (Objects.equals("LOCK", noticeContentIsLockOptional.get())) {
                throw new OperationException("已超过最大数量，请于24小时后一会重新尝试");
            }
        }
        //计数
        stringRedisService.increment(ipCountKey, 1);
        stringRedisService.increment(noticeContentCountKey, 1);

        String codeKey;
        String code = null;
        if (Objects.equals(SysConst.NoticeType.PHONE.getType(), noticeType)) {
            codeKey = CacheKeys.getSmsCodeKey(codeType, noticeContent);
            code = RandomStringUtils.randomNumeric(verificationCodeProperties.getCodeLen());
            stringRedisService.setContainExpire(codeKey, code, verificationCodeProperties.getCodeExpires(), TimeUnit.MINUTES);
            shortMessageTool.sendShortMessageFromCode(noticeContent, code, codeType);
        }
        if (Objects.equals(SysConst.NoticeType.EMAIL.getType(), noticeType)) {
            codeKey = CacheKeys.getEmailCodeKey(codeType, noticeContent);
            code = RandomStringUtils.randomNumeric(verificationCodeProperties.getCodeLen());
            stringRedisService.setContainExpire(codeKey, code, verificationCodeProperties.getCodeExpires(), TimeUnit.MINUTES);
            throw new OperationException("暂不支持");
        }
        com.example.school.common.mysql.entity.VerificationCodeLog verificationCodeLog = new com.example.school.common.mysql.entity.VerificationCodeLog(noticeContent, noticeType, code, codeType, ip, DateUtils.currentDateTime());
        verificationCodeLogService.save(verificationCodeLog);
    }

    @Override
    public boolean validateCode(String noticeContent, String noticeType, String code, String codeType) {
        String codeKey = getCodeCache(noticeContent, noticeType, codeType);
        Optional<String> stringOptional = stringRedisService.get(codeKey);
        if (stringOptional.isPresent()) {
            String redisCode = stringOptional.get();
            return Objects.equals(redisCode, code);
        }
        return false;
    }

    @Override
    public void deleteCode(String noticeContent, String noticeType, String code, String codeType) {
        String codeKey  = getCodeCache(noticeContent, noticeType, codeType);
        stringRedisService.delete(codeKey);
    }

    private String getCodeCache(String noticeContent, String noticeType, String codeType) {
        String codeKey = null;
        if (Objects.equals(SysConst.NoticeType.PHONE.getType(), noticeType)) {
            codeKey = CacheKeys.getSmsCodeKey(codeType, noticeContent);
        }
        if (Objects.equals(SysConst.NoticeType.EMAIL.getType(), noticeType)) {
            codeKey = CacheKeys.getEmailCodeKey(codeType, noticeContent);
        }
        return codeKey;
    }


    private void contentLock(String countKey, String isLockKey, Integer codeMaxCount, Integer codeMaxExpires) {
        stringRedisService.get(countKey).ifPresent(count -> {
            if (Integer.parseInt(count) >= codeMaxCount) {
                stringRedisService.expire(isLockKey, "LOCK", codeMaxExpires, TimeUnit.HOURS);
                stringRedisService.setNotContainExpire(countKey, "0");
            }
        });
    }
}
