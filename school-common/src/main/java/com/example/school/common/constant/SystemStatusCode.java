package com.example.school.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/21 13:55
 * description: 状态码
 */
@AllArgsConstructor
@Getter
public enum SystemStatusCode {

    SUCCESS(0, "success", "成功"),
    FAILED(1, "failed", "失败"),

    USER_NORMAL(1000, "userNormal", "用户状态:正常"),
    USER_FROZEN(1001, "userFrozen", "用户状态:冻结"),
    USER_EXPIRE(1002, "userExpire", "用户状态:到期"),
    USER_DELETE(1003, "userDelete", "用户状态:已删除"),
    USER_NOT_FOUND(1004, "userNotFound", "用户状态:未找到"),

    ISSUED_JWT_SUCCESS(2000, "issuedJwtSuccess", "jwt 签发成功"),
    ISSUED_JWT_FAILED(2001, "issuedJwtFailed", "jwt 签发失败"),

    NEW_JWT(3000, "newJwt", "accessToken过期，refreshToken未过期，返回新的jwt"),
    ACCESS_TOKEN_EXPIRE(30001, "accessTokenExpire", "accessToken过期"),

    JWT_EXPIRE(3001, "jwtExpire", "jwt 已过期,通知客户端重新登录"),
    JWT_ERROR(3002, "jwtError", "jwt 认证失败无效,包括用户认证失败或者jwt令牌错误无效或者用户未登录"),
    JWT_NOT_FOUND(3003, "jwtNotFound", "jwt未找到"),
    JWT_DIFFERENT_PLACES(3004, "jwtDifferentPlaces", "jwt异地登陆"),

    PARAMS_VALIDATION_FAILED(4000, "paramsValidationFailed", "参数异常"),

    SC_UNAUTHORIZED(401, "unauthorized", "无权限访问"),
    SC_NOT_FOUND(401, "notFound", "没有找到页面"),
    SC_BAD_REQUEST(400, "badRequest", "参数解析失败"),
    SC_METHOD_NOT_ALLOWED(405, "methodNotAllowed", "不支持当前请求方"),
    SC_UNSUPPORTED_MEDIA_TYPE(415, "unsupportedMediaType", "不支持当前媒体类型"),
    SC_INTERNAL_SERVER_ERROR(500, "internalServerError", "系统错误");

    private Integer code;
    private String name;
    private String msg;
}
