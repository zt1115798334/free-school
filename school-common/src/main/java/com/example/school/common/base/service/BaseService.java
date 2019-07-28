package com.example.school.common.base.service;

import com.example.school.common.constant.SysConst;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface BaseService<T, ID> extends ConstantService {

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


     default Map<String, SearchFilter> getEffectiveState() {
        Map<String, SearchFilter> filters = Maps.newHashMap();
        filters.put("inReleaseState", new SearchFilter("state", SysConst.State.IN_RELEASE.getType(), Operator.NEQ));
        filters.put("lowerShelfState", new SearchFilter("state", SysConst.State.LOWER_SHELF.getType(), Operator.NEQ));
        filters.put("deleteState", new SearchFilter("deleteState", UN_DELETED, Operator.EQ));
        return filters;
    }
}
