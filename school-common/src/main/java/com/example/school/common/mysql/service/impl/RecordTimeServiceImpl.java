package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.RecordTime;
import com.example.school.common.mysql.repo.RecordTimeRepository;
import com.example.school.common.mysql.service.RecordTimeService;
import com.example.school.common.mysql.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.Operator;
import static com.example.school.common.base.service.SearchFilter.bySearchFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 21:52
 * description:
 */
@AllArgsConstructor
@Service
public class RecordTimeServiceImpl implements RecordTimeService {


    private final RecordTimeRepository recordTimeRepository;

    private final TopicService topicService;


    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public RecordTime save(RecordTime recordTime) {
        Long id = recordTime.getId();
        if (id != null && id != 0L) {
            Optional<RecordTime> recordTimeOptional = findByIdNotDelete(id);
            if (recordTimeOptional.isPresent()) {
                RecordTime recordTimeDB = recordTimeOptional.get();
                recordTimeDB.setTitle(recordTime.getTitle());
                recordTimeDB.setDescribeContent(recordTime.getDescribeContent());
                return recordTimeRepository.save(recordTimeDB);
            }
            return null;
        } else {
            recordTime.setState(IN_RELEASE);
            recordTime.setCreatedTime(currentDateTime());
            recordTime.setDeleteState(UN_DELETED);
            return recordTimeRepository.save(recordTime);
        }
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void deleteById(Long aLong) {
        recordTimeRepository.findById(aLong).ifPresent(recordTime -> {
            recordTime.setDeleteState(DELETED);
            recordTimeRepository.save(recordTime);
        });
    }

    @Override
    public Optional<RecordTime> findByIdNotDelete(Long aLong) {
        return recordTimeRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public Page<RecordTime> findPageByEntity(RecordTime recordTime) {
        return null;
    }

    @Override
    public RoRecordTime saveRecordTime(RecordTime recordTime) {
        recordTime = this.save(recordTime);
        return topicService.resultRoRecordTime(recordTime, recordTime.getUserId(), TOPIC_TYPE_5, ZAN_TOPIC);

    }

    @Override
    public void deleteRecordTime(Long id) {
        this.deleteById(id);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void modifyRecordTimeSateToNewRelease(Long id) {
        recordTimeRepository.findById(id).ifPresent(recordTime -> {
            recordTime.setState(NEW_RELEASE);
            recordTimeRepository.save(recordTime);
        });
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void modifyRecordTimeSateToAfterRelease(List<Long> userId) {
        recordTimeRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public RoRecordTime findRecordTime(Long id, Long userId) throws OperationException {
        RecordTime recordTime = this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
        return topicService.resultRoRecordTime(recordTime, userId, TOPIC_TYPE_5, ZAN_TOPIC);
    }


    @Override
    public CustomPage<RoRecordTime> findRecordTimeEffectivePage(RecordTime recordTime, Long userId) {
        Map<String, SearchFilter> filters = getRecordTimeFilter(getEffectiveState(), recordTime);
        return getRoRecordTimeCustomPage(recordTime, userId, filters);

    }

    @Override
    public CustomPage<RoRecordTime> findRecordTimeUserPage(RecordTime recordTime, Long userId) {
        Map<String, SearchFilter> filters = getRecordTimeFilter(getEffectiveState(), recordTime);
        filters.put("userId", new SearchFilter("userId", recordTime.getUserId(), Operator.EQ));
        return getRoRecordTimeCustomPage(recordTime, userId, filters);

    }

    private CustomPage<RoRecordTime> getRoRecordTimeCustomPage(RecordTime recordTime, Long userId, Map<String, SearchFilter> filters) {
        Specification<RecordTime> specification = bySearchFilter(filters.values());
        Pageable pageable = PageUtils.buildPageRequest(recordTime);
        Page<RecordTime> page = recordTimeRepository.findAll(specification, pageable);
        return topicService.resultRoRecordTimePage(page, userId, TOPIC_TYPE_5, ZAN_TOPIC);
    }

    private Map<String, SearchFilter> getRecordTimeFilter(Map<String, SearchFilter> filters, RecordTime recordTime) {
        if (recordTime.getStartDateTime() != null && recordTime.getEndDateTime() != null) {
            filters.put("createdTimeStart", new SearchFilter("createdTime", recordTime.getStartDateTime(), Operator.GTE));
            filters.put("createdTimeEnd", new SearchFilter("createdTime", recordTime.getEndDateTime(), Operator.LTE));
        }
        return filters;
    }
}
