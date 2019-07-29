package com.example.school.common.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/1/26 18:50
 * description:
 */
public class NumberUtils {

    /**
     * 设置小数点
     *
     * @param number 数
     * @param scale  小数点位数
     * @return 结果
     */
    public static Double numberScale(Number number, int scale) {
        return new BigDecimal(number.doubleValue())
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 加法
     *
     * @param number 数值
     * @return 结果
     */
    public static double sum(Double... number) {
        return Arrays.stream(number).filter(Objects::nonNull).mapToDouble(Number::doubleValue).sum();
    }

    /**
     * 加法
     *
     * @param number 数值
     * @return 结果
     */
    public static long sum(Long... number) {
        return Arrays.stream(number).filter(Objects::nonNull).mapToLong(Number::longValue).sum();
    }

    /**
     * 计算百分比 保留小数点后scale位 表达式为 a/(a+b)*100%
     *
     * @param num1  被除数
     * @param num2  除数
     * @param scale 保留位数
     * @return 结果
     */
    public static Double percentage(Number num1, Number num2, int scale) {
        double result = 0D;
        BigDecimal numBig1 = new BigDecimal(String.valueOf(num1));
        BigDecimal numBig2 = new BigDecimal(String.valueOf(num2));
        if (numBig2.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal calcResult = numBig1.divide(numBig2, scale, BigDecimal.ROUND_HALF_DOWN)
                    .subtract(BigDecimal.ONE)
                    .abs()
                    .multiply(new BigDecimal(100));
            result = calcResult.doubleValue();
        }
        return result;
    }

    /**
     * 除法 保留小数点后4位
     *
     * @param num1 被除数
     * @param num2 除数
     * @return 结果
     */
    public static Double divide(Number num1, Number num2) {
        double result = 0D;
        BigDecimal numBig1 = new BigDecimal(String.valueOf(num1));
        BigDecimal numBig2 = new BigDecimal(String.valueOf(num2));
        if (numBig2.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal calcResult = numBig1.divide(numBig2, 0, BigDecimal.ROUND_HALF_DOWN);
            result = calcResult.doubleValue();
        }
        return result;
    }

    /**
     * 除法 保留小数点后4位
     *
     * @param num1 被除数
     * @param num2 除数
     * @return 结果
     */
    public static Double division(Number num1, Number num2) {
        return division(num1, num2, 4);
    }

    /**
     * 除法 保留小数点后4位 表达式为 a/b*100%
     *
     * @param num1  被除数
     * @param num2  除数
     * @param scale 保留位数
     * @return 结果
     */
    public static Double division(Number num1, Number num2, int scale) {
        double result = 0D;
        BigDecimal numBig1 = new BigDecimal(String.valueOf(num1));
        BigDecimal numBig2 = new BigDecimal(String.valueOf(num2));
        if (numBig2.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal calcResult = numBig1.divide(numBig2, scale, BigDecimal.ROUND_HALF_DOWN)
                    .multiply(new BigDecimal(100));
            result = calcResult.doubleValue();
        }
        return result;
    }

    public static String intToBinary(int num) {
        char[] chs = new char[Integer.SIZE];
        for (int i = 0; i < Integer.SIZE; i++) {
            chs[Integer.SIZE - 1 - i] = (char) ((num >> i & 1) + '0');
        }
        return new String(chs);
    }

}
