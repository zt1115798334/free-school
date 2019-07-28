package com.example.school.common.utils;

import org.hibernate.id.UUIDHexGenerator;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 流水号生成器
 *
 * @author zhangtong
 * Created by on 2017/11/16
 */
public class SNUtil {

    private final static String str62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static int pixLen = 36;
    private static volatile int pixOne = 0;
    private static volatile int pixTwo = 0;
    private static volatile int pixThree = 0;
    private static volatile int pixFour = 0;

    private static final AtomicInteger ATOM_INT = new AtomicInteger(0);
    private static final int MAX_36 = 36 * 36 * 36 * 36;

    /**
     * 生成短时间内不会重复的长度为15位的字符串。<br/>
     * 生成策略为获取自1970年1月1日零时零分零秒至当前时间的毫秒数的16进制字符串值，该字符串值为11位<br/>
     * 并追加四位"0-z"的自增字符串.<br/>
     * 如果系统时间设置为大于<b>2304-6-27 7:00:26<b/>的时间，将会报错！<br/>
     * 由于系统返回的毫秒数与操作系统关系很大，所以本方法并不准确。本方法可以保证在系统返回的一个毫秒数内生成36的4次方个（1679616）ID不重复。<br/>
     * 经过测试：该方法效率 比 create15_2 方法快一倍
     * @return 15位短时间不会重复的字符串。<br/>
     * @author wangbo
     * @since JDK1.6
     */
    public final static synchronized String create15() {
        StringBuilder sb = new StringBuilder(15);// 创建一个StringBuilder
        sb.append(Long.toHexString(System.currentTimeMillis()));// 先添加当前时间的毫秒值的16进制
        pixFour++;
        if (pixFour == pixLen) {
            pixFour = 0;
            pixThree++;
            if (pixThree == pixLen) {
                pixThree = 0;
                pixTwo++;
                if (pixTwo == pixLen) {
                    pixTwo = 0;
                    pixOne++;
                    if (pixOne == pixLen) {
                        pixOne = 0;
                    }
                }
            }
        }
        return sb.append(str62.charAt(pixOne)).append(str62.charAt(pixTwo)).append(str62.charAt(pixThree)).append(str62.charAt(pixFour))
                .toString();
    }
    @SuppressWarnings("unused")
    private final static String create15_2() {
        StringBuilder sb = new StringBuilder(15);
        sb.append(Long.toHexString(System.currentTimeMillis()));
        ATOM_INT.compareAndSet(MAX_36, 0);
        String str = longTo36(ATOM_INT.incrementAndGet());
        if (str.length() == 1) {
            sb.append("000").append(str);
        } else if (str.length() == 2) {
            sb.append("00").append(str);
        } else if (str.length() == 3) {
            sb.append("0").append(str);
        } else {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 10进制转任意进制
     * @param num Long型值
     * @param base 转换的进制
     * @return 任意进制的字符形式
     */
    private static final String ten2Any(long num, int base) {
        StringBuilder sb = new StringBuilder(7);
        while (num != 0) {
            sb.append(str62.charAt((int) (num % base)));
            num /= base;
        }
        return sb.reverse().toString();
    }

    /**
     * 将一个Long 值 转换为 62进制
     * @param num
     * @return
     */
    public static final String longTo62(long num) {
        return ten2Any(num, 62);
    }

    private static final String longTo36(long num) {
        return ten2Any(num, 36);
    }



    public String generate() {
        return new StringBuilder(36).append(format(getIP())).append(sep)
                .append(format(getJVM())).append(sep)
                .append(format(getHiTime())).append(sep)
                .append(format(getLoTime())).append(sep)
                .append(format(getCount())).toString();
    }

    private String sep = "";
    private static final int IP;
    static {
        int ipadd;
        try {
            ipadd = toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }
    private static short counter = (short) 0;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    /**
     * Unique in a local network
     */
    protected int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    private short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    private int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    /**
     * Unique across JVMs on this machine (unless they load this class in the
     * same quater second - very unlikely)
     */
    protected int getJVM() {
        return JVM;
    }

    protected String format(int intVal) {
        String formatted = Integer.toHexString(intVal);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortVal) {
        String formatted = Integer.toHexString(shortVal);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    protected short getCount() {
        synchronized (UUIDHexGenerator.class) {
            if (counter < 0)
                counter = 0;
            return counter++;
        }
    }

}
