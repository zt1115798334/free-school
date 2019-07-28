package com.example.school.common.utils;

import com.google.common.collect.Lists;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/17 10:49
 * description: 集合工具类
 */
public class ListUtils {
    /**
     * 并集
     *
     * @param list1 参数1
     * @param list2 参数2
     * @return List<String>
     */
    public static List<String> listUnion(List<String> list1, List<String> list2) {
        List<String> listAll = Lists.newArrayList();
        listAll.addAll(list1);
        listAll.addAll(list2);
        return listAll.stream().distinct().collect(toList());
    }

    /**
     * 并集
     *
     * @param list1 参数1
     * @param list2 参数2
     * @return List<String>
     */
    public static List<Long> listUnionLong(List<Long> list1, List<Long> list2) {
        List<Long> listAll = Lists.newArrayList();
        listAll.addAll(list1);
        listAll.addAll(list2);
        return listAll.stream().distinct().collect(toList());
    }

    /**
     * 交集
     *
     * @param list1 参数1
     * @param list2 参数2
     * @return List<String>
     */
    public static List<String> listIntersect(List<String> list1, List<String> list2) {
        return list1.stream().filter(list2::contains).collect(toList());
    }

    /**
     * 差集
     *
     * @param list1 参数1
     * @param list2 参数2
     * @return List<String>
     */
    public static List<String> listExcept(List<String> list1, List<String> list2) {
        return list1.stream().filter(item -> !list2.contains(item)).collect(toList());
    }

    /**
     * 差集
     *
     * @param list1 参数1
     * @param list2 参数2
     * @return List<String>
     */
    public static List<Long> listExceptLong(List<Long> list1, List<Long> list2) {
        return list1.stream().filter(item -> !list2.contains(item)).collect(toList());
    }

    /**
     * 差集
     *
     * @param list1 参数1
     * @param list2 参数2
     * @return List<Integer>
     */
    public static List<Integer> listExceptInteger(List<Integer> list1, List<Integer> list2) {
        return list1.stream().filter(item -> !list2.contains(item)).collect(toList());
    }
}
