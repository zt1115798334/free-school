package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoKnowing;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.repo.KnowingRepository;
import com.example.school.common.mysql.service.*;
import com.example.school.common.utils.DateUtils;
import com.google.common.base.Objects;
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
public class KnowingImpl implements Knowing {

    private final KnowingRepository knowingRepository;

    private final Topic topicService;

    private final User userService;

    private final Comment commentService;

    private final Collection collectionService;

    @Override
    public com.example.school.common.mysql.entity.Knowing save(com.example.school.common.mysql.entity.Knowing knowing) {
        Long id = knowing.getId();
        if (id != null && id != 0L) {
            Optional<com.example.school.common.mysql.entity.Knowing> knowingOptional = findByIdNotDelete(id);
            if (knowingOptional.isPresent()) {
                com.example.school.common.mysql.entity.Knowing knowingDB = knowingOptional.get();
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
    public Optional<com.example.school.common.mysql.entity.Knowing> findByIdNotDelete(Long aLong) {
        return knowingRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<com.example.school.common.mysql.entity.Knowing> findByIdsNotDelete(List<Long> id) {
        return knowingRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<com.example.school.common.mysql.entity.Knowing> findPageByEntity(com.example.school.common.mysql.entity.Knowing knowing) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RoKnowing saveKnowing(com.example.school.common.mysql.entity.Knowing knowing, Long userId) {
        com.example.school.common.mysql.entity.User user = userService.findByUserId(userId);
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
        com.example.school.common.mysql.entity.Knowing knowing = this.findKnowing(id);
        if (!Objects.equal(knowing.getState(), SysConst.State.SOLVE.getType())) {
            userService.increaseIntegral(knowing.getUserId(), knowing.getIntegral());
        }
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
        com.example.school.common.mysql.entity.Knowing knowing = this.findKnowing(id);
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
    public com.example.school.common.mysql.entity.Knowing findKnowing(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoKnowing findRoKnowing(Long id, Long userId) {
        com.example.school.common.mysql.entity.Knowing knowing = this.findKnowing(id);
        this.incrementKnowingBrowsingVolume(id);
        return topicService.resultRoKnowing(knowing, userId);
    }


    @Override
    public PageImpl<RoKnowing> findKnowingEffectivePage(com.example.school.common.mysql.entity.Knowing knowing, Long userId) {
        List<SearchFilter> filters = getKnowingFilter(getEffectiveState(), knowing);
        return getRoKnowingCustomPage(knowing, userId, filters);

    }

    @Override
    public PageImpl<RoKnowing> findKnowingUserPage(com.example.school.common.mysql.entity.Knowing knowing, Long userId) {
        List<SearchFilter> filters = getKnowingFilter(getEffectiveState(), knowing);
        filters.add(new SearchFilter("userId", knowing.getUserId(), Operator.EQ));
        return getRoKnowingCustomPage(knowing, userId, filters);

    }

    @Override
    public PageImpl<RoKnowing> findKnowingCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, TOPIC_TYPE_3, customPage);
        List<com.example.school.common.mysql.entity.Knowing> knowingList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoKnowingPage(new PageImpl<>(knowingList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoKnowing> getRoKnowingCustomPage(com.example.school.common.mysql.entity.Knowing knowing, Long userId, List<SearchFilter> filters) {
        Specification<com.example.school.common.mysql.entity.Knowing> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(knowing);
        Page<com.example.school.common.mysql.entity.Knowing> page = knowingRepository.findAll(specification, pageable);
        return topicService.resultRoKnowingPage(page, userId);
    }

    private List<SearchFilter> getKnowingFilter(List<SearchFilter> filters, com.example.school.common.mysql.entity.Knowing knowing) {
        return getTopicFilter(filters, knowing.getSearchArea(), knowing.getSearchValue(), knowing.getStartDateTime(), knowing.getEndDateTime());
    }
}
