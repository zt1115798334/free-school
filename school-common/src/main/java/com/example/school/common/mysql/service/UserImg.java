package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/24 10:16
 * description:
 */
public interface UserImg extends Base<com.example.school.common.mysql.entity.UserImg, Long> {

    com.example.school.common.mysql.entity.UserImg saveUserImg(HttpServletRequest request, Long userId);

    com.example.school.common.mysql.entity.UserImg findUserImgUrlByOn(Long userId);

    Map<Long, com.example.school.common.mysql.entity.UserImg> findUserImgUrlByOn(List<Long> userIdList);

    void modifyUserImg(Long userId, Long imgId);

}
