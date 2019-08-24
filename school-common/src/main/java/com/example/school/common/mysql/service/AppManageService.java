package com.example.school.common.mysql.service;

import com.example.school.common.base.service.BaseService;
import com.example.school.common.mysql.entity.AppManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/09 10:21
 * description:
 */
public interface AppManageService extends BaseService<AppManage, Long> {


    AppManage saveAppManager(AppManage appManage);

    AppManage findAppManager(String systemType);

    AppManage saveAppManager(HttpServletRequest request) throws IOException;

    void downAppFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
