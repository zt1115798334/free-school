package com.example.school.common.base.service;

import com.example.school.common.constant.SysConst;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.*;
import static com.example.school.common.base.service.SearchFilter.Operator;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface Base<T, ID> extends Constant {

    default T save(T t) {
        return null;
    }

    default Iterable<T> saveAll(Iterable<T> t) {
        return t;
    }

    default void deleteById(ID id) {
    }

    default void deleteById(List<ID> id) {
    }

    default void deleteAll(Iterable<T> t) {
    }

    default Optional<T> findById(ID id) {
        return Optional.empty();
    }

    default List<T> findByIds(List<ID> id) {
        return null;
    }

    default List<T> findByIdsOrder(List<ID> id) {
        return null;
    }

    default Optional<T> findByIdNotDelete(ID id) {
        return Optional.empty();
    }

    default List<T> findByIdsNotDelete(List<ID> id) {
        return null;
    }

    default List<T> findAll() {
        return null;
    }

    default List<T> findListByEntity(T t) {
        return null;
    }

    default Page<T> findPageByEntity(T t) {
        return null;
    }


    default List<SearchFilter> getEffectiveState() {
        List<SearchFilter> filters = Lists.newArrayList();
        filters.add(new SearchFilter("state", SysConst.State.IN_RELEASE.getType(), Operator.NEQ));
        filters.add(new SearchFilter("state", SysConst.State.LOWER_SHELF.getType(), Operator.NEQ));
        filters.add(new SearchFilter("deleteState", UN_DELETED, Operator.EQ));
        return filters;
    }

    default List<SearchFilter> getTopicFilter(List<SearchFilter> filters, String searchArea, String searchValue, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (StringUtils.isNotEmpty(searchValue)) {
            if (Objects.equal(SysConst.SearchArea.ALL.getType(), searchArea)) {
                filters.add(new SearchFilter("title", searchValue, Operator.LIKE, Logic.OR));
                filters.add(new SearchFilter("describeContent", searchValue, Operator.LIKE, Logic.OR));
            }
            if (Objects.equal(SysConst.SearchArea.TITLE.getType(), searchArea)) {
                filters.add(new SearchFilter("title", searchValue, Operator.LIKE, Logic.OR));
            }
            if (Objects.equal(SysConst.SearchArea.CONTENT.getType(), searchArea)) {
                filters.add(new SearchFilter("describeContent", searchValue, Operator.LIKE, Logic.OR));
            }
        }
        if (startDateTime != null && endDateTime != null) {
            filters.add(new SearchFilter("createdTime", startDateTime, Operator.GTE));
            filters.add(new SearchFilter("createdTime", endDateTime, Operator.LTE));
        }
        return filters;
    }

}