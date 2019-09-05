package com.example.school.app;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.constant.SysConst;
import com.example.school.common.utils.StringTestUtils;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/27 10:22
 * description:
 */
public class BaseAutoLoginTest {

    private WebClient webClient = null;

    private static String authorization = "";
    private static final String deviceInfo = SysConst.DeviceInfo.MOBILE.getType();

    @Before
    public void before() {
//        String username = "11157983341";
//        String password = "1115798334";

//        String username = "15130097582";
//        String password = "15130097582";
        String username = "18812165843";
        String password = "dxsg666666";

        webClient = WebClient.builder().baseUrl("http://127.0.0.1:8093").build();
        Map<String, Object> map = Maps.newHashMap();
        map.put("username", username);
        map.put("password", password);
        map.put("registrationId", "1507bfd3f7e3fe6a43c");
        String loginUrl = "/app/login/login";
        authorization = postNoAuthorization(loginUrl, map);
        System.out.println("authorization = " + authorization);
    }

    private String postNoAuthorization(String url, Map<String, Object> paramMap) {
        Mono<String> params = Mono.just(StringTestUtils.splicingParams(paramMap));
        Mono<JSONObject> stringFlux = webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params, String.class)
                .retrieve()
                .bodyToMono(JSONObject.class);
        return stringFlux.map(jsonObject -> {
            JSONObject meta = jsonObject.getJSONObject("meta");
            Boolean success = meta.getBoolean("success");
            if (success) {
                JSONObject data = jsonObject.getJSONObject("data");
                return data.getString("accessToken");
            }
            return "";
        }).block();
    }

    protected void postParams(String url, Map<String, Object> paramMap) {
        Mono<String> params = Mono.just(StringTestUtils.splicingParams(paramMap));
        webClient.post().uri(url)
                .header("authorization", authorization)
                .header("deviceInfo", deviceInfo)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params, String.class)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .blockLast();
    }

    protected void postJSONObject(String url, Object obj) {
        Mono<JSONObject> params = Mono.just(JSONObject.parseObject(JSONObject.toJSONString(obj)));
        webClient.post().uri(url)
                .header("authorization", authorization)
                .header("deviceInfo", deviceInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .body(params, JSONObject.class)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .blockLast();
    }
}
