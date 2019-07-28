package com.example.school.common.utils;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/22 10:49
 * description:
 */
public class MD5Utils {

    /**
     * 生成Token
     *
     * @return String
     */
    public static String generateToken(String tokenKey, long currentTime) {
        // 获取 10 为毫秒值
        String keys = tokenKey + currentTime + tokenKey;
        return MD5(keys);
    }

    /**
     * MD5加密
     *
     * @param s 字符串
     * @return String
     */
    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
