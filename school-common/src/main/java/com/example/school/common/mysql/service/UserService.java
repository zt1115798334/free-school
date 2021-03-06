package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.entity.vo.VoUser;
import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.User;
import org.springframework.data.domain.PageImpl;

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
public interface UserService extends Base<User, Long> {

    void updateLastLoginTime(Long userId);

    void saveUserStudent(String phone, String password);

    void saveUserStudentPresident(String phone, String password);

    void saveUser(String phone, String password, String accountType);

    RoUser saveUser(User user);

    void saveSchoolAdministration(Long userId, Short schoolCode, String studentId, String studentPwd);

    void relieveSchoolAdministration(String account);

    void validateSchoolAdministration(Short schoolCode, String studentId, String studentPwd);

    void saveSchoolAdministration(String phone, Short schoolCode, String studentId, String studentPwd);

    void modifyPassword(String phone, String password);

    void deleteUser(Long userId);

    void normalUser(Long userId);

    void increaseIntegral(Long sellerUserId, Long integral);

    void reduceIntegral(Long buyerUserId, Long integral);

    Optional<User> findOptByUserId(Long userId);

    Optional<User> findOptByAccount(String account);

    Optional<User> findOptByPhone(String phone);

    User findByPhoneUnDelete(String phone);

    User findByUserId(Long userId);

    RoUser findRoUserByUserId(Long userId);

    List<RoUser> findRoUserByUserId(List<Long> userIdList);

    Map<Long, RoUser> findMapRoUserByUserId(List<Long> userIdList);

    PageImpl<RoUser> findRoUser(VoUser voUser);

    void validatePhoneByRegister(String phone);

    void validatePhoneByForget(String phone);

}
