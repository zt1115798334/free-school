package com.example.school.app;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.utils.StringTestUtils;
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
public class BaseTest {

    private WebClient webClient = null;

    @Before
    public void before() {
//        webClient = WebClient.builder().baseUrl("http://152.136.145.193:80").build();
        webClient = WebClient.builder().baseUrl("http://127.0.0.1:8093").build();
    }

    protected void postParams(String url, Map<String, Object> paramMap) {
        Mono<String> params = Mono.just(StringTestUtils.splicingParams(paramMap));
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params, String.class)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .blockLast();
    }

    protected void postJSON(String url, Object obj) {
        Mono<JSONObject> params = Mono.just(JSONObject.parseObject(JSONObject.toJSONString(obj)));
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(params, JSONObject.class)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .blockLast();
    }
}
