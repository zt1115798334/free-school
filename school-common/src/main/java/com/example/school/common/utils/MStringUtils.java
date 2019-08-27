package com.example.school.common.utils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/11/17 13:43
 * description:
 */
public class MStringUtils {

    /**
     * 请求参数格式转换 aop 接口使用
     *
     * @param parameterNames  parameterNames
     * @param parameterValues parameterValues
     * @return String
     */
    public static String parseParams(String[] parameterNames, Object[] parameterValues) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("[");
        int length = parameterNames.length;
        for (int i = 0; i < length; i++) {
            String parameterName = parameterNames[i];
            Object parameterValueObj = parameterValues[i];
            Class<?> parameterValueClazz = parameterValueObj.getClass();
            String parameterValue;
            if (parameterValueClazz.isPrimitive() ||
                    parameterValueClazz == String.class) {
                parameterValue = parameterValueObj.toString();
            } else if (parameterValueObj instanceof Serializable) {
                parameterValue = JSON.toJSONString(parameterValueObj);
            } else {
                parameterValue = parameterValueObj.toString();
            }
            stringBuffer.append(parameterName).append(":").append(parameterValue).append(" ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /**
     * unicode编码
     *
     * @param string string
     * @return string
     */
    public static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * unicode解码
     *
     * @param string
     * @return string
     */
    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
