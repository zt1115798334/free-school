package com.example.school.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 15:31
 * description:
 */
public class NetworkUtil {

    /**
     * 从Request对象中获得客户端IP，处理了HTTP代理服务器和Nginx的反向代理截取了ip
     *
     * @param request
     * @return ip
     */
    public static String getLocalIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        }
        return ip;
    }

    private static boolean checkIP(String ip) {
        return ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
     *
     * @param strIp
     * @return
     */
    public static Long ipToLong(String strIp) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     *
     * @param longIp
     * @return
     */
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer();
        //直接右移24位
        sb.append((longIp >>> 24));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append((longIp & 0x000000FF));
        return sb.toString();
    }


}
