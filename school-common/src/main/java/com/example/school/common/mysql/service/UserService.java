package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:27
 * description:
 */
public interface UserService extends BaseService<User, Long> {

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户id
     */
    void updateLastLoginTime(Long userId);

    void saveUserOrdinary(String phone, String password) throws OperationException;

    RoUser saveUserOrdinary(User user) throws OperationException;

    void modifyPasswordOrdinary(String phone, String password) throws OperationException;

    Optional<User> findOptByUserId(Long userId);

    Optional<User> findOptByUserId(String account);


    Optional<User> findOptByPhone(String phone);


    User findByPhoneUnDelete(String phone) throws OperationException;

    User findByUserId(Long userId) throws OperationException;

    RoUser findRoUserByUserId(Long userId);

    List<RoUser> findRoUserByUserId(List<Long> userIdList);

    Map<Long, RoUser> findMapRoUserByUserId(List<Long> userIdList);

    void validatePhoneByRegister(String phone) throws OperationException;

    void validatePhoneByForget(String phone) throws OperationException;

}
