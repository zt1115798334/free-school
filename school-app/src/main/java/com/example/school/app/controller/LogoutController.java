package com.example.school.app.controller;

import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.constant.SysConst;
import com.example.school.common.utils.NetworkUtil;
import com.example.school.shiro.aop.SaveLog;
import com.example.school.shiro.base.CurrentUser;
import com.example.school.shiro.shiro.service.CommonLoginService;
import com.example.school.shiro.shiro.utils.RequestResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/13 16:12
 * description: 注册 控制器
 */
@Api(tags = "注册")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/logout")
public class LogoutController extends AbstractController implements CurrentUser {

    private final CommonLoginService commonLoginService;

    private static final String deviceInfo = SysConst.DeviceInfo.MOBILE.getType();

    /**
     * 注销
     *
     * @param registrationId 极光推送id
     * @return ResultMessage
     */
    @ApiOperation(value = "注销")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "logout")
    @SaveLog(desc = "app注销")
    public ResultMessage logout(HttpServletRequest request,
                                @NotBlank(message = "极光推送id不能为空") @RequestParam String registrationId) {
        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        Long currentUserId = getCurrentUserId();
        commonLoginService.logout(currentUserId, ip, deviceInfo, registrationId);
        return success("退出成功");
    }

}
