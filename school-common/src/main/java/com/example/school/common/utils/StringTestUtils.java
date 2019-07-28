package com.example.school.common.utils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/27 16:33
 * description:
 */
public class StringTestUtils {

    public static String splicingParams(Map<String, Object> params) {
        return params.entrySet()
                .stream().map(entity -> entity.getKey() + "=" + entity.getValue())
                .collect(Collectors.joining("&"));
    }


}
