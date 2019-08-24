package com.example.school.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.base.web.AbstractController;
import com.example.school.common.constant.SysConst;
import com.example.school.common.mysql.entity.AppManage;
import com.example.school.common.mysql.service.AppManageService;
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
@RestController
@RequestMapping(value = "app/appVersion")
@AllArgsConstructor
public class AppVersionController extends AbstractController {

    private final AppManageService appManageService;

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

    @PostMapping(value = "saveAndroidAppFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultMessage saveAndroidAppFile(HttpServletRequest request) throws IOException {
        appManageService.saveAppManager(request);
        return success("保存成功");
    }
}
