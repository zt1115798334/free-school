package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.RecordTime;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 21:52
 * description:
 */
public interface RecordTimeService extends BaseService<RecordTime, Long> {

    RoRecordTime saveRecordTime(RecordTime recordtime);

    void deleteRecordTime(Long id);

    void modifyRecordTimeSateToNewRelease(Long id);

    void modifyRecordTimeSateToAfterRelease(List<Long> userId);

    RoRecordTime findRecordTime(Long id, Long userId) throws OperationException;

    CustomPage<RoRecordTime> findRecordTimeEffectivePage(RecordTime recordtime, Long userId);

    CustomPage<RoRecordTime> findRecordTimeUserPage(RecordTime recordtime, Long userId);

}
