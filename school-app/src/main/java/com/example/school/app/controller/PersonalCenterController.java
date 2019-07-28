package com.example.school.app.controller;

import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.entity.vo.VoStorageUser;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.service.UserImgService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.utils.change.VoChangeEntityUtils;
import com.example.school.shiro.aop.DistributedLock;
import com.example.school.shiro.aop.SaveLog;
import com.example.school.shiro.base.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/23 1:12
 * description:
 */
@Api(tags = "个人中心")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/transaction")
public class PersonalCenterController extends AbstractController implements CurrentUser, ConstantService {

    private final UserService userService;

    private final UserImgService userImgService;

    @ApiOperation(value = "显示用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findUserInfo")
    public ResultMessage findUserInfo() {
        RoUser user = userService.findRoUserByUserId(getCurrentUserId());
        return success(user);
    }

    @ApiOperation(value = "保存用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveUserInfo")
    @SaveLog(desc = "保存用户信息")
    @DistributedLock
    public ResultMessage saveUserInfo(@RequestBody VoStorageUser voStorageUser) throws OperationException {
        User user = VoChangeEntityUtils.changeUser(voStorageUser);
        user.setId(getCurrentUserId());
        RoUser roUser = userService.saveUserOrdinary(user);
        return success(roUser);
    }

    @ApiOperation(value = "保存用户图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveUserImg")
    @SaveLog(desc = "保存用户图片信息")
    @DistributedLock
    public ResultMessage saveUserImg(HttpServletRequest request) {
        userImgService.saveUserImg(request, getCurrentUserId());
        return success("保存成功");
    }
}
