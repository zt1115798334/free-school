package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.service.Base;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 21:52
 * description:
 */
public interface RecordTime extends Base<com.example.school.common.mysql.entity.RecordTime, Long> {

    RoRecordTime saveRecordTime(com.example.school.common.mysql.entity.RecordTime recordTime);

    void deleteRecordTime(Long id);

    void modifyRecordTimeSateToNewRelease(Long id);

    void modifyRecordTimeSateToAfterRelease(List<Long> userId);

    void incrementRecordTimeBrowsingVolume(Long id);

    com.example.school.common.mysql.entity.RecordTime findRecordTime(Long id) ;

    RoRecordTime findRoRecordTime(Long id, Long userId) ;

    PageImpl<RoRecordTime> findRecordTimeEffectivePage(com.example.school.common.mysql.entity.RecordTime recordTime, Long userId);

    PageImpl<RoRecordTime> findRecordTimeUserPage(com.example.school.common.mysql.entity.RecordTime recordTime, Long userId);

    PageImpl<RoRecordTime> findRecordTimeCollectionPage(CustomPage customPage, Long userId);


}
