package com.example.school.common.base.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:56
 * description:
 */
public class SearchFilter {
    public enum Operator {
        EQ, NEQ, LIKE, GT, LT, GTE, LTE, IN
    }

    public enum Logic {
        AND, OR
    }

    private String fieldName;
    private Object value;
    private Operator operator;
    private Logic logic;

    public SearchFilter(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.logic = Logic.AND;
    }

    public SearchFilter(String fieldName, Object value, Operator operator, Logic logic) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.logic = logic;
    }

 /*   *//**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     *//*
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = Maps.newHashMap();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isBlank((String) value)) {
                continue;
            }

            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length != 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[0];
            Operator operator = Operator.valueOf(names[1]);
            Logic logic = Logic.valueOf(names[2]);

            // 创建searchFilter
            SearchFilter filter = new SearchFilter(filedName, value, operator, logic);
            filters.put(key, filter);
        }

        return filters;
    }*/

    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters) {
        return (Specification<T>) (root, query, builder) -> {
            if (filters != null && !filters.isEmpty()) {

                List<Predicate> predicateAndList = Lists.newArrayList();
                List<Predicate> predicateOrList = Lists.newArrayList();
                for (SearchFilter filter : filters) {
                    // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                    Object filterValue = filter.value;
                    String[] names = StringUtils.split(filter.fieldName, ".");
                    Path expression = root.get(names[0]);
                    for (int i = 1; i < names.length; i++) {
                        expression = expression.get(names[i]);
                    }
                    if (Objects.equals(filter.logic, Logic.AND)) {
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicateAndList.add(builder.equal(expression, filterValue));
                                break;
                            case NEQ:
                                predicateAndList.add(builder.notEqual(expression, filterValue));
                                break;
                            case LIKE:
                                predicateAndList.add(builder.like(expression, "%" + filterValue + "%"));
                                break;
                            case GT:
                                predicateAndList.add(builder.greaterThan(expression, (Comparable) filterValue));
                                break;
                            case LT:
                                predicateAndList.add(builder.lessThan(expression, (Comparable) filterValue));
                                break;
                            case GTE:
                                predicateAndList.add(builder.greaterThanOrEqualTo(expression, (Comparable) filterValue));
                                break;
                            case LTE:
                                predicateAndList.add(builder.lessThanOrEqualTo(expression, (Comparable) filterValue));
                                break;
                            case IN:
                                CriteriaBuilder.In in = builder.in(expression);
                                ((List) filterValue).forEach(val -> {
                                    in.value(val);
                                });
                                predicateAndList.add(in);
                                break;
                        }
                    }
                    if (Objects.equals(filter.logic, Logic.OR)) {
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicateOrList.add(builder.or(builder.equal(expression, filterValue)));
                                break;
                            case NEQ:
                                predicateOrList.add(builder.or(builder.notEqual(expression, filterValue)));
                                break;
                            case LIKE:
                                predicateOrList.add(builder.or(builder.like(expression, "%" + filterValue + "%")));
                                break;
                        }
                    }

                }
                Predicate predicateAnd = builder.and(predicateAndList.toArray(new Predicate[0]));

                Predicate predicateOr = builder.or(predicateOrList.toArray(new Predicate[0]));

                // 将所有条件用 and 联合起来
                if (!predicateAndList.isEmpty() && !predicateOrList.isEmpty()) {
                    return query.where(predicateAnd, predicateOr).getRestriction();
                } else if (!predicateAndList.isEmpty()) {
                    return predicateAnd;
                } else if (!predicateOrList.isEmpty()) {
                    return predicateOr;
                }
            }

            return builder.conjunction();
        };
    }
}
