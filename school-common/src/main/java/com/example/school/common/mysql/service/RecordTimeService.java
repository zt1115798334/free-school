package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.RecordTime;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 21:52
 * description:
 */
public interface RecordTimeService extends BaseService<RecordTime, Long> {

    RoRecordTime saveRecordTime(RecordTime recordTime);

    void deleteRecordTime(Long id);

    void modifyRecordTimeSateToNewRelease(Long id);

    void modifyRecordTimeSateToAfterRelease(List<Long> userId);

    void incrementRecordTimeBrowsingVolume(Long id);

    RecordTime findRecordTime(Long id) ;

    RoRecordTime findRoRecordTime(Long id, Long userId) ;

    PageImpl<RoRecordTime> findRecordTimeEffectivePage(RecordTime recordTime, Long userId);

    PageImpl<RoRecordTime> findRecordTimeUserPage(RecordTime recordTime, Long userId);

    PageImpl<RoRecordTime> findRecordTimeCollectionPage(CustomPage customPage, Long userId);


}
