package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.repo.RecordTimeRepository;
import com.example.school.common.mysql.service.Collection;
import com.example.school.common.mysql.service.RecordTime;
import com.example.school.common.mysql.service.Topic;
import com.example.school.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class RecordTimeImpl implements RecordTime {

    private final RecordTimeRepository recordTimeRepository;

    private final Topic topicService;

    private final Collection collectionService;

    @Override
    public com.example.school.common.mysql.entity.RecordTime save(com.example.school.common.mysql.entity.RecordTime recordTime) {
        Long id = recordTime.getId();
        if (id != null && id != 0L) {
            Optional<com.example.school.common.mysql.entity.RecordTime> recordTimeOptional = findByIdNotDelete(id);
            if (recordTimeOptional.isPresent()) {
                com.example.school.common.mysql.entity.RecordTime recordTimeDB = recordTimeOptional.get();
                recordTimeDB.setTitle(recordTime.getTitle());
                recordTimeDB.setDescribeContent(recordTime.getDescribeContent());
                recordTimeDB.setUpdatedTime(DateUtils.currentDateTime());
                return recordTimeRepository.save(recordTimeDB);
            }
            return null;
        } else {
            recordTime.setBrowsingVolume(0L);
            recordTime.setState(IN_RELEASE);
            recordTime.setCreatedTime(currentDateTime());
            recordTime.setDeleteState(UN_DELETED);
            return recordTimeRepository.save(recordTime);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        recordTimeRepository.findById(aLong).ifPresent(recordTime -> {
            recordTime.setDeleteState(DELETED);
            recordTimeRepository.save(recordTime);
        });
    }

    @Override
    public Optional<com.example.school.common.mysql.entity.RecordTime> findByIdNotDelete(Long aLong) {
        return recordTimeRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<com.example.school.common.mysql.entity.RecordTime> findByIdsNotDelete(List<Long> id) {
        return recordTimeRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<com.example.school.common.mysql.entity.RecordTime> findPageByEntity(com.example.school.common.mysql.entity.RecordTime recordTime) {
        return null;
    }

    @Override
    public RoRecordTime saveRecordTime(com.example.school.common.mysql.entity.RecordTime recordTime) {
        recordTime = this.save(recordTime);
        return topicService.resultRoRecordTime(recordTime, recordTime.getUserId());

    }

    @Override
    public void deleteRecordTime(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyRecordTimeSateToNewRelease(Long id) {
        recordTimeRepository.findById(id).ifPresent(recordTime -> {
            recordTime.setState(NEW_RELEASE);
            recordTimeRepository.save(recordTime);
        });
    }

    @Override
    public void modifyRecordTimeSateToAfterRelease(List<Long> userId) {
        recordTimeRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public void incrementRecordTimeBrowsingVolume(Long id) {
        recordTimeRepository.incrementBrowsingVolume(id);
    }

    @Override
    public com.example.school.common.mysql.entity.RecordTime findRecordTime(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoRecordTime findRoRecordTime(Long id, Long userId) {
        com.example.school.common.mysql.entity.RecordTime recordTime = this.findRecordTime(id);
        this.incrementRecordTimeBrowsingVolume(id);
        return topicService.resultRoRecordTime(recordTime, userId);
    }


    @Override
    public PageImpl<RoRecordTime> findRecordTimeEffectivePage(com.example.school.common.mysql.entity.RecordTime recordTime, Long userId) {
        List<SearchFilter> filters = getRecordTimeFilter(getEffectiveState(), recordTime);
        return getRoRecordTimeCustomPage(recordTime, userId, filters);

    }

    @Override
    public PageImpl<RoRecordTime> findRecordTimeUserPage(com.example.school.common.mysql.entity.RecordTime recordTime, Long userId) {
        List<SearchFilter> filters = getRecordTimeFilter(getEffectiveState(), recordTime);
        filters.add(new SearchFilter("userId", recordTime.getUserId(), Operator.EQ));
        return getRoRecordTimeCustomPage(recordTime, userId, filters);

    }

    @Override
    public PageImpl<RoRecordTime> findRecordTimeCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, TOPIC_TYPE_5, customPage);
        List<com.example.school.common.mysql.entity.RecordTime> recordTimeList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoRecordTimePage(new PageImpl<>(recordTimeList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoRecordTime> getRoRecordTimeCustomPage(com.example.school.common.mysql.entity.RecordTime recordTime, Long userId, List<SearchFilter> filters) {
        Specification<com.example.school.common.mysql.entity.RecordTime> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(recordTime);
        Page<com.example.school.common.mysql.entity.RecordTime> page = recordTimeRepository.findAll(specification, pageable);
        return topicService.resultRoRecordTimePage(page, userId);
    }

    private List<SearchFilter> getRecordTimeFilter(List<SearchFilter> filters, com.example.school.common.mysql.entity.RecordTime recordTime) {
        return getTopicFilter(filters, recordTime.getSearchArea(), recordTime.getSearchValue(), recordTime.getStartDateTime(), recordTime.getEndDateTime());
    }

}
