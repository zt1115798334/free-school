package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.IdPageEntity;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.mysql.entity.Collection;
import com.example.school.common.mysql.repo.CollectionRepository;
import com.example.school.common.mysql.service.CollectionService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.Operator;
import static java.util.stream.Collectors.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CollectionServiceImpl implements CollectionService {


    private final CollectionRepository collectionRepository;

    @Override
    public Collection save(Collection collection) {
        collection.setCreatedTime(currentDateTime());
        return collectionRepository.save(collection);
    }

    @Override
    public void enableOnCollection(Long userId, Long topicId, Short topicType) {
        Optional<Collection> collectionOptional = collectionRepository.findByUserIdAndTopicIdAndTopicType(userId, topicId, topicType);
        Collection collection = collectionOptional.orElse(new Collection(userId, topicId, topicType, ON));
        collection.setCollectionState(ON);
        this.save(collection);
    }

    @Override
    public void enableOffCollection(Long userId, Long topicId, Short topicType) {
        Optional<Collection> collectionOptional = collectionRepository.findByUserIdAndTopicIdAndTopicType(userId, topicId, topicType);
        Collection collection = collectionOptional.orElse(new Collection(userId, topicId, topicType, OFF));
        collection.setCollectionState(OFF);
        this.save(collection);
    }

    @Override
    public Map<Long, Long> collectionState(Long userId, List<Long> topicId, Short topicType) {
        List<Collection> collectionList = collectionRepository.findByUserIdAndTopicIdInAndTopicTypeAndCollectionState(userId, topicId, topicType, ON);
        return collectionList.stream().collect(groupingBy(Collection::getTopicId, counting()));
    }

    @Override
    public PageImpl<Long> findCollection(Long userId, Short topicType, IdPageEntity pageEntity) {
        List<SearchFilter> filters = Lists.newArrayList();
        filters.add(new SearchFilter("userId", userId, Operator.EQ));
        filters.add(new SearchFilter("topicType", topicType, Operator.EQ));
        filters.add(new SearchFilter("collectionState", ON, Operator.EQ));
        Pageable pageable = PageUtils.buildPageRequest(pageEntity);
        Page<Collection> collectionPage = collectionRepository.findAll(SearchFilter.bySearchFilter(filters), pageable);
        return new PageImpl<>(collectionPage.stream().map(Collection::getTopicId).collect(toList()),
                collectionPage.getPageable(),
                collectionPage.getTotalElements());
    }
}
