package com.example.school.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.constant.SysConst;
import com.example.school.common.mysql.entity.AppManage;
import com.example.school.common.mysql.service.AppManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/7/9 11:05
 * description:
 */
@Api(tags = "版本信息")
@RestController
@RequestMapping(value = "app/appVersion")
@AllArgsConstructor
public class AppVersionController extends AbstractController {

    private final AppManageService appManageService;

    @ApiOperation(value = "获取版本信息")
    @GetMapping(value = "findAndroidAppInfo")
    public ResultMessage findAndroidAppInfo() {
        AppManage appManage = appManageService.findAppManager(SysConst.AppSystemType.ANDROID.getType());
        JSONObject result = new JSONObject();
        result.put("version", appManage.getVersion());
        result.put("versionCode", appManage.getVersionCode());
        result.put("content", appManage.getContent());
        result.put("fileUrl", "/app/appVersion/findAndroidAppFile");
        return success(result);
    }

    @GetMapping(value = "findAndroidAppFile", produces = {
            MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void findAndroidAppFile(HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            appManageService.downAppFile(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "保存反馈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "version", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "versionCode", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "appFile", dataType = "String")
    })
    @PostMapping(value = "saveAndroidAppFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultMessage saveAndroidAppFile(HttpServletRequest request) throws IOException {
        appManageService.saveAppManager(request);
        return success("保存成功");
    }
}
