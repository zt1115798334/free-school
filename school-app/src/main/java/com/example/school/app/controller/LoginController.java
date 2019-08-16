package com.example.school.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.service.SchoolAdministrationService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.service.VerificationCodeService;
import com.example.school.common.utils.NetworkUtil;
import com.example.school.common.validation.NoticeType;
import com.example.school.shiro.aop.DistributedLock;
import com.example.school.shiro.aop.SaveLog;
import com.example.school.shiro.shiro.service.CommonLoginService;
import com.example.school.shiro.shiro.token.PasswordToken;
import com.example.school.shiro.shiro.utils.RequestResponseUtil;
import com.google.common.base.Objects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/28 17:38
 * description: 登录接口
 */

@Api(tags = "用户登录")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/login")
public class LoginController extends AbstractController {

    private final CommonLoginService commonLoginService;

    private final UserService userService;

    private final VerificationCodeService verificationCodeService;

    private static final String deviceInfo = SysConst.DeviceInfo.MOBILE.getType();

    /**
     * 登录
     *
     * @param username       用户名
     * @param password       密码
     * @param registrationId 极光推送，用户设备标识
     * @return ResultMessage
     */
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", defaultValue = "15130097582"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", defaultValue = "15130097582"),
            @ApiImplicitParam(paramType = "query", name = "registrationId", dataType = "String")
    })
    @PostMapping(value = "login")
    @SaveLog(desc = "app登录")
    @DistributedLock
    public ResultMessage login(HttpServletRequest request,
                               @NotBlank(message = "账户不能为空") @RequestParam String username,
                               @NotBlank(message = "密码不能为空") @RequestParam String password,
                               @NotBlank(message = "用户设备标识不能为空") @RequestParam String registrationId) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            String LoginType = SysConst.LoginType.AJAX.getType();
            PasswordToken token = new PasswordToken(username, password, LoginType);
            String accessToken = commonLoginService.login(token, Boolean.TRUE, ip, deviceInfo, registrationId);
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            result.put("accountType", user.getAccountType());
            return success("登录成功", result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }


    /**
     * 验证码登录
     *
     * @param request          request
     * @param noticeContent    通知内容
     * @param verificationCode 验证码
     * @param noticeType       通知类型{@link SysConst.NoticeType}
     * @return ResultMessage
     */
    @ApiOperation(value = "验证码登录")
    @PostMapping(value = "verificationCodeLogin")
    @SaveLog(desc = "验证码登录")
    public ResultMessage verificationCodeLogin(HttpServletRequest request,
                                               @NotBlank(message = "手机号或者邮件不能为空") @RequestParam(value = "noticeContent") String noticeContent,
                                               @NotBlank(message = "验证码不能为空") @RequestParam(value = "verificationCode") String verificationCode,
                                               @NoticeType @NotBlank(message = "验证类型不能为空") @RequestParam(value = "noticeType") String noticeType) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            String LoginType = SysConst.LoginType.VERIFICATION_CODE.getType();
            PasswordToken token = new PasswordToken(noticeContent, "888888", true, verificationCode, noticeType, LoginType);
            String accessToken = commonLoginService.login(token, true, ip, deviceInfo);
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            return success("登录成功", result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    @PostMapping(value = "sendVerificationCodeByLogin")
    @ApiOperation(value = "发送验证码执行登录操作")
    @SaveLog(desc = "发送验证码执行登录操作")
    public ResultMessage sendVerificationCodeByLogin(HttpServletRequest request,
                                                     @NotBlank(message = "手机号或者邮件不能为空") @RequestParam(value = "noticeContent") String noticeContent,
                                                     @NoticeType @NotBlank(message = "验证类型不能为空") @RequestParam(value = "noticeType") String noticeType) {
        if (Objects.equal(SysConst.NoticeType.PHONE.getType(), noticeType)) {
            userService.findByPhoneUnDelete(noticeContent);  //验证手机号的合法性
        } else {
            throw new OperationException("暂时只支持短信验证码登录");
        }

        String ip = NetworkUtil.getLocalIp(request);
        verificationCodeService.sendCode(ip, noticeContent, noticeType, SysConst.VerificationCodeType.LOGIN.getType());
        return success("发送成功");
    }


    @ApiOperation(value = "验证手机号执行注册操作")
    @PostMapping(value = "validatePhoneByRegister")
    public ResultMessage validatePhoneByRegister(@NotBlank(message = "手机号不能为空")
                                                 @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                 @RequestParam String phone) {
        userService.validatePhoneByRegister(phone);
        return success();

    }


    @ApiOperation(value = "发送手机验证码执行注册操作")
    @PostMapping(value = "sendPhoneCodeByRegister")
    @SaveLog(desc = "发送手机验证码执行注册操作")
    public ResultMessage sendPhoneCodeByRegister(HttpServletRequest request,
                                                 @NotBlank(message = "手机号不能为空")
                                                 @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                 @RequestParam String phone) {
        String ip = NetworkUtil.getLocalIp(request);
        userService.validatePhoneByRegister(phone);
        verificationCodeService.sendCode(ip, phone, SysConst.NoticeType.PHONE.getType(), SysConst.VerificationCodeType.REGISTER.getType());
        return success("发送成功");
    }

    @ApiOperation(value = "注册")
    @PostMapping(value = "register")
    @SaveLog(desc = "注册")
    public ResultMessage register(@NotBlank(message = "手机号不能为空")
                                  @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                  @RequestParam String phone,
                                  @NotBlank(message = "验证码不能为空")
                                  @RequestParam String code,
                                  @NotBlank(message = "密码不能为空")
                                  @RequestParam String password) {
        boolean state = verificationCodeService.validateCode(phone, SysConst.NoticeType.PHONE.getType(), code, SysConst.VerificationCodeType.REGISTER.getType());
        if (state) {
            userService.saveUserStudent(phone, password);
            return success("保存成功");
        } else {
            return failure("验证失败");
        }
    }

    @ApiOperation(value = "保存教务信息")
    @PostMapping(value = "saveSchoolAdministrationFromRegister")
    @SaveLog(desc = "保存教务信息")
    @DistributedLock
    public ResultMessage saveSchoolAdministrationFromRegister(@NotBlank(message = "手机号不能为空")
                                                              @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                              @RequestParam String phone,
                                                              @NotBlank(message = "学校不能为空")
                                                              @RequestParam String school,
                                                              @NotBlank(message = "教务处账户不能为空")
                                                              @RequestParam String studentId,
                                                              @NotBlank(message = "教务处密码不能为空")
                                                              @RequestParam String studentPwd) {
        userService.saveSchoolAdministration(phone, school, studentId, studentPwd);
        return success("保存成功");
    }


    @PostMapping(value = "validatePhoneCodeByForget")
    @ApiOperation(value = "检验手机验证码执行重置密码操作")
    public ResultMessage validatePhoneCodeByForget(@NotBlank(message = "手机号不能为空")
                                                   @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                   @RequestParam String phone) {
        userService.validatePhoneByForget(phone);
        return success();

    }

    @PostMapping(value = "sendPhoneCodeByForget")
    @ApiOperation(value = "发送手机验证码执行重置密码操作")
    @SaveLog(desc = "发送手机验证码执行重置密码操作")
    public ResultMessage sendPhoneCodeByForget(HttpServletRequest request,
                                               @NotBlank(message = "手机号不能为空")
                                               @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                               @RequestParam String phone) {
        userService.validatePhoneByForget(phone);
        String ip = NetworkUtil.getLocalIp(request);
        verificationCodeService.sendCode(ip, phone, SysConst.NoticeType.PHONE.getType(), SysConst.VerificationCodeType.FORGET.getType());
        return success("发送成功");
    }

    @PostMapping(value = "modifyPassword")
    @ApiOperation(value = "修改密码")
    @SaveLog(desc = "修改密码")
    @DistributedLock
    public ResultMessage modifyPassword(@NotBlank(message = "手机号不能为空")
                                        @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                        @RequestParam String phone,
                                        @NotBlank(message = "验证码不能为空")
                                        @RequestParam String code,
                                        @NotBlank(message = "密码不能为空") @RequestParam String password) {
        boolean state = verificationCodeService.validateCode(phone, SysConst.NoticeType.PHONE.getType(), code, SysConst.VerificationCodeType.FORGET.getType());
        if (state) {
            userService.modifyPassword(phone, password);
            return success("保存成功");
        } else {
            return failure("验证失败");
        }

    }
}
