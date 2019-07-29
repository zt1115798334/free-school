package com.example.school.common.service;

import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/28 14:29
 * description:
 */
public interface VerificationCodeService {

    /**
     * 发送验证码
     *
     * @param ip            ip
     * @param noticeContent 需要通知的内容
     * @param noticeType    通知内容的类型 短信或者邮箱
     * @param codeType      通知的类型
     */
    void sendCode(String ip, String noticeContent, String noticeType, String codeType) ;

    /**
     * 检验验证码
     *
     * @param noticeContent 需要通知的内容
     * @param noticeType    通知内容的类型 短信或者邮箱{@link SysConst.NoticeType}
     * @param code          验证码
     * @param codeType      通知的类型
     * @return boolean
     */
    boolean validateCode(String noticeContent, String noticeType, String code, String codeType);

    /**
     * 检验验证码
     *
     * @param noticeContent 需要通知的内容
     * @param noticeType    通知内容的类型 短信或者邮箱{@link SysConst.NoticeType}
     * @param code          验证码
     * @param codeType      通知的类型
     */
    void deleteCode(String noticeContent, String noticeType, String code, String codeType);
}
