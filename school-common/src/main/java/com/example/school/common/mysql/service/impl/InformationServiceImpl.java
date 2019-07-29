package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoInformation;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Information;
import com.example.school.common.mysql.repo.InformationRepository;
import com.example.school.common.mysql.service.CollectionService;
import com.example.school.common.mysql.service.InformationService;
import com.example.school.common.mysql.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.Operator;
import static com.example.school.common.base.service.SearchFilter.bySearchFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 15:11
 * description:
 */
@AllArgsConstructor
@Service
public class InformationServiceImpl implements InformationService {


    private final InformationRepository informationRepository;

    private final TopicService topicService;

    private final CollectionService collectionService;

    @Override
    public Information save(Information information) {
        Long id = information.getId();
        if (id != null && id != 0L) {
            Optional<Information> informationOptional = findByIdNotDelete(id);
            if (informationOptional.isPresent()) {
                Information informationDB = informationOptional.get();
                informationDB.setTitle(information.getTitle());
                informationDB.setDescribeContent(information.getDescribeContent());
                return informationRepository.save(informationDB);
            }
            return null;
        } else {
            information.setBrowsingVolume(0L);
            information.setState(IN_RELEASE);
            information.setCreatedTime(currentDateTime());
            information.setDeleteState(UN_DELETED);
            return informationRepository.save(information);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        informationRepository.findById(aLong).ifPresent(information -> {
            information.setDeleteState(DELETED);
            informationRepository.save(information);
        });
    }

    @Override
    public Optional<Information> findByIdNotDelete(Long aLong) {
        return informationRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<Information> findByIdsNotDelete(List<Long> id) {
        return informationRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<Information> findPageByEntity(Information information) {
        return null;
    }

    @Override
    public RoInformation saveInformation(Information information) {
        information = this.save(information);
        return topicService.resultRoInformation(information, information.getUserId());

    }

    @Override
    public void deleteInformation(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyInformationSateToNewRelease(Long id) {
        informationRepository.findById(id).ifPresent(information -> {
            information.setState(NEW_RELEASE);
            informationRepository.save(information);
        });
    }

    @Override
    public void modifyInformationSateToAfterRelease(List<Long> userId) {
        informationRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void incrementInformationBrowsingVolume(Long id) {
        informationRepository.incrementBrowsingVolume(id);
    }

    @Override
    public Information findInformation(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoInformation findRoInformation(Long id, Long userId) {
        Information information = this.findInformation(id);
        this.incrementInformationBrowsingVolume(id);
        return topicService.resultRoInformation(information, userId);
    }


    @Override
    public PageImpl<RoInformation> findInformationEffectivePage(Information information, Long userId) {
        List<SearchFilter> filters = getInformationFilter(getEffectiveState(), information);
        return getRoInformationCustomPage(information, userId, filters);

    }

    @Override
    public PageImpl<RoInformation> findInformationUserPage(Information information, Long userId) {
        List<SearchFilter> filters = getInformationFilter(getEffectiveState(), information);
        filters.add(new SearchFilter("userId", information.getUserId(), Operator.EQ));
        return getRoInformationCustomPage(information, userId, filters);

    }

    @Override
    public PageImpl<RoInformation> findInformationCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, TOPIC_TYPE_2, customPage);
        List<Information> knowingList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoInformationPage(new PageImpl<>(knowingList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoInformation> getRoInformationCustomPage(Information information, Long userId, List<SearchFilter> filters) {
        Specification<Information> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(information);
        Page<Information> page = informationRepository.findAll(specification, pageable);
        return topicService.resultRoInformationPage(page, userId);
    }

    private List<SearchFilter> getInformationFilter(List<SearchFilter> filters, Information information) {
        if (information.getStartDateTime() != null && information.getEndDateTime() != null) {
            filters.add(new SearchFilter("createdTime", information.getStartDateTime(), Operator.GTE));
            filters.add(new SearchFilter("createdTime", information.getEndDateTime(), Operator.LTE));
        }
        return filters;
    }

}
