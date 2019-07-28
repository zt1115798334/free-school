package com.example.school.common.base.web;

import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.constant.SystemStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/13 16:17
 * description:
 */
@RestController
@RequestMapping("/error")
public class ErrorStatusController {

    /**
     * 401页面
     */
    @GetMapping(value = "/401")
    public ResultMessage error_401() {
        SystemStatusCode scUnauthorized = SystemStatusCode.SC_UNAUTHORIZED;
        return new ResultMessage().error(scUnauthorized.getCode(), scUnauthorized.getName());
    }

    /**
     * 404页面
     */
    @GetMapping(value = "/404")
    public ResultMessage error_404() {
        SystemStatusCode scNotFound = SystemStatusCode.SC_NOT_FOUND;
        return new ResultMessage().error(scNotFound.getCode(), scNotFound.getName());
    }

    /**
     * 500页面
     */
    @GetMapping(value = "/500")
    public ResultMessage error_500() {
        SystemStatusCode scInternalServerError = SystemStatusCode.SC_INTERNAL_SERVER_ERROR;
        return new ResultMessage().error(scInternalServerError.getCode(), scInternalServerError.getName());
    }
}
