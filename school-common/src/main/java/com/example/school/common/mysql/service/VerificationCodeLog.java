package com.example.school.common.mysql.service;


import com.example.school.common.base.service.Base;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
public interface VerificationCodeLog extends Base<com.example.school.common.mysql.entity.VerificationCodeLog, Long> {

    /**
     * 删除时间段内的所有用户日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
