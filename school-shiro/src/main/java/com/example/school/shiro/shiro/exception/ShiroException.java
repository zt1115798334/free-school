package com.example.school.shiro.shiro.exception;

import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.constant.SystemStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/26 17:11
 * description:
 */
@Slf4j
@RestControllerAdvice
public class ShiroException {

    private final SystemStatusCode scInternalServerError = SystemStatusCode.SC_INTERNAL_SERVER_ERROR;

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public ResultMessage unauthorizedExceptionHandler(HttpServletResponse response, Exception exception) {
        return new ResultMessage().error(scInternalServerError.getCode(), "无权访问，请联系管理员");
    }


    /**
     * AccountException
     *
     * @param ex ex
     * @return ResultMessage
     */
    @ExceptionHandler(AccountException.class)
    public ResultMessage accountException(AccountException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.USER_EXPIRE.getCode(), ex.getMessage());
    }

}
