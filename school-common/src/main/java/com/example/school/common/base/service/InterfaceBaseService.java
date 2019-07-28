package com.example.school.common.base.service;

import com.example.school.common.utils.RSAUtils;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/1/16 15:54
 * description:
 */
public interface InterfaceBaseService {

    default StringBuffer splicingUrl(String url) {
        return new StringBuffer();
    }

    default StringBuffer splicingUrl(String url, int pageNumber, int pageSize) {
        return new StringBuffer();
    }

    default Map<String, String> createdHeader(String publicKey, String prefix, String suffix) {
        long timeMillis = System.currentTimeMillis();

        byte[] code1 = new byte[0];
        try {
            code1 = RSAUtils.encryptByPublicKey((prefix + timeMillis + suffix).getBytes(), Base64.decodeBase64(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = Base64.encodeBase64String(code1);
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("token", token);
        headerMap.put("timestamp", String.valueOf(timeMillis));
        return headerMap;
    }
}
