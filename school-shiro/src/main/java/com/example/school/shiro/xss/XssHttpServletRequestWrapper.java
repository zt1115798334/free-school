package com.example.school.shiro.xss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.school.common.utils.XssUtils;
import com.google.common.collect.Maps;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/4 16:52
 * description: 对xss，sql注入的过滤的具体的实现
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final static int BUFFER_SIZE = 4096;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 重写  数组参数过滤
     *
     * @param parameter parameter
     * @return String[]
     */
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = filterParamString(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> primary = super.getParameterMap();
        Map<String, String[]> result = Maps.newHashMap();
        for (Map.Entry<String, String[]> entry : primary.entrySet()) {
            result.put(entry.getKey(), filterEntryString(entry.getValue()));
        }
        return result;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    public String getParameter(String parameter) {
        return filterParamString(super.getParameter(parameter));
    }

    @Override
    public String getHeader(String name) {
        return filterParamString(super.getHeader(name));
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(filterParamString(cookie.getValue()));
            }
        }
        return cookies;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream inputStream = super.getInputStream();
        String strCont = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        String result = "";
        try {
            Object toJSON = JSON.parse(strCont);

            if (toJSON instanceof JSONObject) {
                JSONObject jsonObj = JSONObject.parseObject(strCont);
                JSONObject resultObj = jsonObjectCleanXSS(jsonObj);
                result = resultObj.toJSONString();
            }
            if (toJSON instanceof JSONArray) {
                JSONArray jsonArray = JSONArray.parseArray(strCont);
                List<JSONObject> collect = jsonArray.stream().map(object -> {
                    JSONObject jsonObj = TypeUtils.castToJavaBean(object, JSONObject.class);
                    return jsonObjectCleanXSS(jsonObj);
                }).collect(Collectors.toList());
                result = JSON.toJSONString(collect);
            }

        } catch (Exception ex) {
            result = strCont;
        }

        final ByteArrayInputStream bAis = new ByteArrayInputStream(result.getBytes()); //再封装数据

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return bAis.read();
            }
        };

    }

    private JSONObject jsonObjectCleanXSS(JSONObject jsonObj) {
        JSONObject resultObj = new JSONObject();
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List) {
                JSONArray valueArray = JSON.parseArray(String.valueOf(value));
                List<String> valueList = valueArray.stream()
                        .map(str -> filterParamString(String.valueOf(str)))
                        .collect(Collectors.toList());
                resultObj.put(entry.getKey(), valueList);
            } else {
                resultObj.put(entry.getKey(), filterParamString(String.valueOf(value)));
            }
        }
        return resultObj;
    }


    private static byte[] InputStreamTOByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        return outStream.toByteArray();
    }


    /**
     * 过滤字符串数组不安全内容
     *
     * @param value value
     * @return String[]
     */
    private String[] filterEntryString(String[] value) {
        for (int i = 0; i < value.length; i++) {
            value[i] = filterParamString(value[i]);
        }
        return value;
    }

    /**
     * 过滤字符串不安全内容
     *
     * @param value value
     * @return String
     */
    private String filterParamString(String value) {
        if (null == value) {
            return null;
        }
        // 过滤XSS 和 SQL 注入
        return XssUtils.stripSqlXss(value);
    }

}
