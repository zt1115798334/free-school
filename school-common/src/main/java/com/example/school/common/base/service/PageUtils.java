package com.example.school.common.base.service;

import com.example.school.common.base.entity.IdPageEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:50
 * description:
 */
public class PageUtils {
    /**
     * @param page      当前页
     * @param size      每页条数
     * @param sortName  排序字段
     * @param sortOrder 排序方向
     */
    public static PageRequest buildPageRequest(int page, int size, String sortName, String sortOrder) {
        return Optional.ofNullable(sortName)
                .map(s -> Optional.ofNullable(sortOrder)
                        .map(ss -> {
                            if (StringUtils.equalsIgnoreCase(sortOrder, Sort.Direction.ASC.toString())) {
                                return new Sort(Sort.Direction.ASC, sortName);
                            } else {
                                return new Sort(Sort.Direction.DESC, sortName);
                            }
                        }).orElseGet(() -> new Sort(Sort.Direction.ASC, sortName)))
                .map(sort -> PageRequest.of(page - 1, size, sort))
                .orElse(PageRequest.of(page - 1, size));
    }

    public static PageRequest buildPageRequest(int page, int size, String sortName) {
        return buildPageRequest(page, size, sortName, null);
    }

    public static PageRequest buildPageRequest(int page, int size) {
        return buildPageRequest(page, size, null, null);
    }

    public static PageRequest buildPageRequest(int page, int size, Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

    public static PageRequest buildPageRequest(IdPageEntity page) {
        return buildPageRequest(page.getPageNumber(), page.getPageSize(), page.getSortName(), page.getSortOrder());
    }
}
