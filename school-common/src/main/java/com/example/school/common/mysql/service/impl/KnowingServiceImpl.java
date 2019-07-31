package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoKnowing;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Knowing;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.repo.KnowingRepository;
import com.example.school.common.mysql.service.*;
import com.example.school.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.Operator;
import static com.example.school.common.base.service.SearchFilter.bySearchFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 11:34
 * description:
 */
@AllArgsConstructor
@Service
public class KnowingServiceImpl implements KnowingService {

    private final KnowingRepository knowingRepository;

    private final TopicService topicService;

    private final UserService userService;

    private final CommentService commentService;

    private final CollectionService collectionService;

    @Override
    public Knowing save(Knowing knowing) {
        Long id = knowing.getId();
        if (id != null && id != 0L) {
            Optional<Knowing> knowingOptional = findByIdNotDelete(id);
            if (knowingOptional.isPresent()) {
                Knowing knowingDB = knowingOptional.get();
                knowingDB.setTitle(knowing.getTitle());
                knowingDB.setIntegral(knowing.getIntegral());
                knowingDB.setDescribeContent(knowing.getDescribeContent());
                knowingDB.setUpdatedTime(DateUtils.currentDateTime());
                return knowingRepository.save(knowingDB);
            }
            return null;
        } else {
            knowing.setBrowsingVolume(0L);
            knowing.setState(IN_RELEASE);
            knowing.setCreatedTime(currentDateTime());
            knowing.setDeleteState(UN_DELETED);
            return knowingRepository.save(knowing);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        knowingRepository.findById(aLong).ifPresent(knowing -> {
            knowing.setDeleteState(DELETED);
            knowingRepository.save(knowing);
        });
    }

    @Override
    public Optional<Knowing> findByIdNotDelete(Long aLong) {
        return knowingRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<Knowing> findByIdsNotDelete(List<Long> id) {
        return knowingRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<Knowing> findPageByEntity(Knowing knowing) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RoKnowing saveKnowing(Knowing knowing, Long userId) {
        User user = userService.findByUserId(userId);
        Long integral = knowing.getIntegral();
        if (user.getIntegral() < integral) {
            throw new OperationException("积分不够，请充值");
        }
        knowing.setUserId(userId);
        userService.reduceIntegral(userId, integral);
        knowing = this.save(knowing);
        return topicService.resultRoKnowing(knowing, knowing.getUserId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteKnowing(Long id) {
        Knowing knowing = this.findKnowing(id);
        userService.increaseIntegral(knowing.getUserId(), knowing.getIntegral());
        this.deleteById(id);
    }

    @Override
    public void modifyKnowingSateToNewRelease(Long id) {
        knowingRepository.findById(id).ifPresent(knowing -> {
            knowing.setState(NEW_RELEASE);
            knowingRepository.save(knowing);
        });
    }


    @Override
    public void modifyKnowingSateToAfterRelease(List<Long> userId) {
        knowingRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void adoptKnowingComment(Long id, Long userId, Long commentId, Long commentUserId) {
        Knowing knowing = this.findKnowing(id);
        commentService.adoptComment(commentId, id, TOPIC_TYPE_3);
        userService.increaseIntegral(commentUserId, knowing.getIntegral());
        knowing.setState(SOLVE);
        knowingRepository.save(knowing);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void incrementKnowingBrowsingVolume(Long id) {
        knowingRepository.incrementBrowsingVolume(id);
    }

    @Override
    public Knowing findKnowing(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoKnowing findRoKnowing(Long id, Long userId) {
        Knowing knowing = this.findKnowing(id);
        this.incrementKnowingBrowsingVolume(id);
        return topicService.resultRoKnowing(knowing, userId);
    }


    @Override
    public PageImpl<RoKnowing> findKnowingEffectivePage(Knowing knowing, Long userId) {
        List<SearchFilter> filters = getKnowingFilter(getEffectiveState(), knowing);
        return getRoKnowingCustomPage(knowing, userId, filters);

    }

    @Override
    public PageImpl<RoKnowing> findKnowingUserPage(Knowing knowing, Long userId) {
        List<SearchFilter> filters = getKnowingFilter(getEffectiveState(), knowing);
        filters.add(new SearchFilter("userId", knowing.getUserId(), Operator.EQ));
        return getRoKnowingCustomPage(knowing, userId, filters);

    }

    @Override
    public PageImpl<RoKnowing> findKnowingCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, TOPIC_TYPE_3, customPage);
        List<Knowing> knowingList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoKnowingPage(new PageImpl<>(knowingList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoKnowing> getRoKnowingCustomPage(Knowing knowing, Long userId, List<SearchFilter> filters) {
        Specification<Knowing> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(knowing);
        Page<Knowing> page = knowingRepository.findAll(specification, pageable);
        return topicService.resultRoKnowingPage(page, userId);
    }

    private List<SearchFilter> getKnowingFilter(List<SearchFilter> filters, Knowing knowing) {
        if (knowing.getStartDateTime() != null && knowing.getEndDateTime() != null) {
            filters.add(new SearchFilter("createdTime", knowing.getStartDateTime(), Operator.GTE));
            filters.add(new SearchFilter("createdTime", knowing.getEndDateTime(), Operator.LTE));
        }
        return filters;
    }
}
