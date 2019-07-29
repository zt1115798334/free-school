package com.example.school.common.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.SignRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
public interface SignRecordService extends BaseService<SignRecord, Long> {

    void signIn(Long userId) ;

    void complementSigned(Long userId, Integer dayOfMonth);

    List<JSONObject> signInCalendar(Long userId,LocalDate dateMonth);
}
