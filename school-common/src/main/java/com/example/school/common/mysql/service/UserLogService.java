package com.example.school.common.mysql.service;

import com.example.school.common.base.service.BaseService;
import com.example.school.common.mysql.entity.UserLog;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
public interface UserLogService extends BaseService<UserLog, Long> {

    /**
     * 删除时间段内的所有用户日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

}
