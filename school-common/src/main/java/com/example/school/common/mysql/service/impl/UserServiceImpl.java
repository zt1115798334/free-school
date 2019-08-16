package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.entity.vo.VoUser;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.SchoolAdministration;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.entity.UserImg;
import com.example.school.common.mysql.repo.UserRepository;
import com.example.school.common.mysql.service.PermissionService;
import com.example.school.common.mysql.service.SchoolAdministrationService;
import com.example.school.common.mysql.service.UserImgService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.UserUtils;
import com.example.school.common.utils.change.RoChangeEntityUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.example.school.common.constant.SysConst.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:28
 * description:
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserImgService userImgService;

    private final PermissionService permissionService;

    private final SchoolAdministrationService schoolAdministrationService;


    //冻结
    private static final Short FROZEN = AccountState.FROZEN.getCode();
    private static final Short NORMAL = AccountState.NORMAL.getCode();
    private static final String ADMIN = AccountType.ADMIN.getType();

    @Override
    public User save(User user) {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Long userId = user.getId();
        if (userId != null) {
            Optional<User> pompUserOpt = userRepository.findById(userId);
            if (pompUserOpt.isPresent()) {
                User userO = pompUserOpt.get();
                userO.setAccount(user.getAccount());
                userO.setPassword(user.getPassword());
                userO.setUserName(user.getUserName());
                userO.setSalt(user.getSalt());
                userO.setPhone(user.getPhone());
                userO.setPersonalSignature(user.getPersonalSignature());
                userO.setAccountState(user.getAccountState());
                userO.setUpdatedTime(currentDateTime);
                userO.setDeleteState(ConstantService.UN_DELETED);
                user = userRepository.save(userO);
            }
        } else {
            user.setUserName("用户名");
            user.setCreatedTime(currentDateTime);
            user.setUpdatedTime(currentDateTime);
            user.setDeleteState(ConstantService.UN_DELETED);
            user = userRepository.save(user);
            permissionService.saveSysSystemPermission(user.getId(), user.getAccountType());
        }

        return user;
    }

    @Override
    public void deleteById(Long aLong) {
        Optional<User> userOptional = userRepository.findById(aLong);
        userOptional.ifPresent(user -> {
            user.setDeleteState(DELETED);
            userRepository.save(user);
        });
    }

    @Override
    public Optional<User> findByIdNotDelete(Long id) {
        return userRepository.findByIdAndDeleteState(id, ConstantService.UN_DELETED);
    }

    @Override
    public List<User> findByIdsNotDelete(List<Long> id) {
        return userRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<User> findPageByEntity(User user) {
        List<SearchFilter> filters = buildQueryMap(user);
        Specification<User> specification = SearchFilter.bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(
                user.getPageNumber(),
                user.getPageSize());
        return userRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateLastLoginTime(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.setLastLoginTime(DateUtils.currentDateTime());
            userRepository.save(user);
        });
    }

    @Override
    public void saveUserStudent(String phone, String password) {
        this.saveUser(phone, password, AccountType.STUDENT.getType());
    }

    @Override
    public void saveUserStudentPresident(String phone, String password) {
        this.saveUser(phone, password, AccountType.STUDENT_PRESIDENT.getType());
    }

    @Override
    public void saveUser(String phone, String password, String accountType) {
        this.validatePhoneByRegister(phone);
        String salt = UserUtils.getSalt();
        String encryptPassword = UserUtils.getEncryptPassword(phone, password, salt);
        User user = new User(phone, encryptPassword, salt, UserUtils.getDefaultUserName(phone), phone, Sex
                .UNKNOWN.getCode(), DEFAULT_INTEGRAL, AccountState.NORMAL.getCode(), accountType);
        this.save(user);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RoUser saveUser(User user) {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Long userId = user.getId();
        Optional<User> userOptional = userRepository.findByIdAndDeleteState(userId, UN_DELETED);
        User userDB = userOptional.orElseThrow(() -> new OperationException("用户已被删除"));
        userDB.setUserName(user.getUserName());
        userDB.setPhone(user.getPhone());
        userDB.setPersonalSignature(user.getPersonalSignature());
        userDB.setUpdatedTime(currentDateTime);
        userDB.setDeleteState(ConstantService.UN_DELETED);
        user = userRepository.save(userDB);
        UserImg userImg = userImgService.findUserImgUrlByOn(user.getId());
        return RoChangeEntityUtils.resultRoUser(user, userImg);
    }

    @Override
    public SchoolAdministration saveSchoolAdministration(Long userId, String studentId, String studentPwd) {
        return schoolAdministrationService.saveSchoolAdministration(userId, studentId, studentPwd);
    }

    @Override
    public SchoolAdministration saveSchoolAdministration(String phone, String school, String studentId, String studentPwd) {
        User user = this.findByPhoneUnDelete(phone);
        user.setSchool(school);
        this.save(user);
        return schoolAdministrationService.saveSchoolAdministration(user.getId(), studentId, studentPwd);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void modifyPassword(String phone, String password) {
        this.validatePhoneByForget(phone);
        User user = this.findByPhoneUnDelete(phone);
        String salt = UserUtils.getSalt();
        String encryptPassword = UserUtils.getEncryptPassword(phone, password, salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        this.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        this.deleteById(userId);
    }

    @Override
    public void normalUser(Long userId) {
        User user = this.findByUserId(userId);
        Short accountState = user.getAccountState();
        accountState = Objects.equal(accountState, FROZEN) ? NORMAL : FROZEN;
        user.setAccountState(accountState);
        userRepository.save(user);
    }

    @Override
    public void increaseIntegral(Long sellerUserId, Long integral) {
        User sellerUser = this.findByUserId(sellerUserId);
        sellerUser.setIntegral(sellerUser.getIntegral() + integral);
        userRepository.save(sellerUser);
    }

    @Override
    public void reduceIntegral(Long buyerUserId, Long integral) {
        User buyerUser = this.findByUserId(buyerUserId);
        buyerUser.setIntegral(buyerUser.getIntegral() - integral);
        userRepository.save(buyerUser);
    }

    @Override
    public Optional<User> findOptByUserId(Long userId) {
        return this.findByIdNotDelete(userId);
    }

    @Override
    public Optional<User> findOptByUserId(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public Optional<User> findOptByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User findByPhoneUnDelete(String phone) {
        Optional<User> byPhoneAndDeleteState = userRepository.findByPhoneAndDeleteState(phone, ConstantService.UN_DELETED);
        return byPhoneAndDeleteState.orElseThrow(() -> new OperationException("该用户不存在"));
    }


    @Override
    public User findByUserId(Long userId) {
        return this.findByIdNotDelete(userId).orElseThrow(() -> new OperationException("该用户不存在"));
    }

    @Override
    public RoUser findRoUserByUserId(Long userId) {
        User user = this.findByIdNotDelete(userId).orElse(UserUtils.getDefaultUser());
        UserImg userImg = userImgService.findUserImgUrlByOn(userId);
        return RoChangeEntityUtils.resultRoUser(user, userImg);
    }

    @Override
    public List<RoUser> findRoUserByUserId(List<Long> userIdList) {
        List<User> userList = findByIdsNotDelete(userIdList);
        Map<Long, UserImg> userImgMap = userImgService.findUserImgUrlByOn(userIdList);
        return RoChangeEntityUtils.resultRoUser(userList, userImgMap);
    }

    @Override
    public Map<Long, RoUser> findMapRoUserByUserId(List<Long> userIdList) {
        return this.findRoUserByUserId(userIdList).stream().collect(toMap(RoUser::getUserId, Function.identity()));
    }

    @Override
    public PageImpl<RoUser> findRoUser(VoUser voUser) {
        User user = new User();
        user.setAccount(voUser.getAccount());
        user.setAccountType(voUser.getAccountType());
        user.setUserName(voUser.getUserName());
        Page<User> page = this.findPageByEntity(user);
        List<Long> userIdList = page.stream().map(User::getId).collect(toList());
        Map<Long, UserImg> userImgMap = userImgService.findUserImgUrlByOn(userIdList);
        List<RoUser> roUserList = RoChangeEntityUtils.resultRoUser(page.getContent(), userImgMap);
        return new PageImpl<>(roUserList, page.getPageable(), page.getTotalElements());
    }

    @Override
    public void validatePhoneByRegister(String phone) {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (userOptional.isPresent()) {
            throw new OperationException("该手机号已经注册，请直接登录");
        }
    }

    @Override
    public void validatePhoneByForget(String phone) {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (!userOptional.isPresent()) {
            throw new OperationException("该手机号没有注册，请直接注册");
        }
    }

    private List<SearchFilter> buildQueryMap(User user) {
        List<SearchFilter> filters = Lists.newArrayList();
        if (StringUtils.isNotEmpty(user.getAccount())) {
            filters.add(new SearchFilter("account", user.getAccount(), SearchFilter.Operator.LIKE));
        }
        if (StringUtils.isNotEmpty(user.getUserName())) {
            filters.add(new SearchFilter("userName", user.getUserName(), SearchFilter.Operator.LIKE));
        }
        if (StringUtils.isNotEmpty(user.getAccountType())) {
            filters.add(new SearchFilter("accountType", user.getAccountType(), SearchFilter.Operator.EQ));
        }
        filters.add(new SearchFilter("deleteState", ConstantService.UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }
}
