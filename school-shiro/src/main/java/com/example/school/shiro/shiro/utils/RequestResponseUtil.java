package com.example.school.shiro.shiro.utils;

import com.alibaba.fastjson.JSON;
import com.example.school.shiro.xss.XssHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 20:31
 * description:
 */
@Slf4j
public class RequestResponseUtil {
    /**
     * 取request中的已经被防止XSS，SQL注入过滤过的key value数据封装到map 返回
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParameters(ServletRequest request) {
        Map<String, String> dataMap = new HashMap<>();
        Enumeration enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paraName = (String) enums.nextElement();
            String paraValue = RequestResponseUtil.getRequest(request).getParameter(paraName);
            if (null != paraValue && !"".equals(paraValue)) {
                dataMap.put(paraName, paraValue);
            }
        }
        return dataMap;
    }

    /**
     * 获取request中的body json 数据转化为map
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getRequestBodyMap(ServletRequest request) {
        Map<String, String> dataMap = new HashMap<>();
        // 判断是否已经将 inputStream 流中的 body 数据读出放入 attribute
        if (request.getAttribute("body") != null) {
            // 已经读出则返回attribute中的body
            return (Map<String, String>) request.getAttribute("body");
        } else {
            try {
                Map<String, String> maps = JSON.parseObject(request.getInputStream(), Map.class);
                dataMap.putAll(maps);
                request.setAttribute("body", dataMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dataMap;
        }
    }

    /**
     * 读取request 已经被防止XSS，SQL注入过滤过的 请求参数key 对应的value
     *
     * @param request
     * @param key
     * @return
     */
    public static String getParameter(ServletRequest request, String key) {
        return RequestResponseUtil.getRequest(request).getParameter(key);
    }

    /**
     * 读取request 已经被防止XSS，SQL注入过滤过的 请求头key 对应的value
     *
     * @param request
     * @param key
     * @return
     */
    public static String getHeader(ServletRequest request, String key) {
        return RequestResponseUtil.getRequest(request).getHeader(key);
    }

    /**
     * 取request头中的已经被防止XSS，SQL注入过滤过的 key value数据封装到map 返回
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestHeaders(ServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration enums = RequestResponseUtil.getRequest(request).getHeaderNames();
        while (enums.hasMoreElements()) {
            String name = (String) enums.nextElement();
            String value = RequestResponseUtil.getRequest(request).getHeader(name);
            if (null != value && !"".equals(value)) {
                headerMap.put(name, value);
            }
        }
        return headerMap;
    }

    public static HttpServletRequest getRequest(ServletRequest request) {
        return new XssHttpServletRequestWrapper((HttpServletRequest) request);
    }

    public static void responseWrite(String outStr, ServletResponse response) {

        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = null;
        try {
            printWriter = WebUtils.toHttp(response).getWriter();
            printWriter.write(outStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (null != printWriter) {
                printWriter.close();
            }
        }
    }
}
