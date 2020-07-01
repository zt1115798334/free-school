package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;

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
public interface AppManage extends Base<com.example.school.common.mysql.entity.AppManage, Long> {


    com.example.school.common.mysql.entity.AppManage saveAppManager(com.example.school.common.mysql.entity.AppManage appManage);

    com.example.school.common.mysql.entity.AppManage findAppManager(String systemType);

    com.example.school.common.mysql.entity.AppManage saveAppManager(HttpServletRequest request) throws IOException;

    void downAppFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
