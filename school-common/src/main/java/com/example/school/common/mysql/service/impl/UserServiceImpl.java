package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.entity.UserImg;
import com.example.school.common.mysql.repo.UserRepository;
import com.example.school.common.mysql.service.UserImgService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.UserUtils;
import com.example.school.common.utils.change.RoChangeEntityUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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


    //冻结
    private static final Integer FROZEN = SysConst.AccountState.FROZEN.getCode();
    private static final String ADMIN = SysConst.AccountType.ADMIN.getType();

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
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
        }

        return user;
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
        Map<String, SearchFilter> filters = buildQueryMap(user);
        Specification<User> specification = SearchFilter.bySearchFilter(filters.values());
        Pageable pageable = PageUtils.buildPageRequest(
                user.getPageNumber(),
                user.getPageSize());
        return userRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void updateLastLoginTime(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.setLastLoginTime(DateUtils.currentDateTime());
            userRepository.save(user);
        });
    }

    @Override
    public void saveUserOrdinary(String phone, String password) throws OperationException {
        this.validatePhoneByRegister(phone);
        String salt = UserUtils.getSalt();
        String encryptPassword = UserUtils.getEncryptPassword(phone, password, salt);
        User user = new User(phone, encryptPassword, salt, phone, SysConst.AccountState.normal.getCode(), SysConst.AccountType.ORDINARY.getType());
        this.save(user);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public RoUser saveUserOrdinary(User user) throws OperationException {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Long userId = user.getId();
        Optional<User> userOptional = userRepository.findByIdAndDeleteState(userId, UN_DELETED);
        User userDB = userOptional.orElseThrow(() -> new OperationException("用户已被删除"));

        userDB.setUserName(user.getUserName());
        userDB.setPhone(user.getPhone());
        userDB.setPersonalSignature(user.getPersonalSignature());
        userDB.setAccountState(user.getAccountState());
        userDB.setUpdatedTime(currentDateTime);
        userDB.setDeleteState(ConstantService.UN_DELETED);
        user = userRepository.save(userDB);
        UserImg userImg = userImgService.findUserImgUrlByOn(user.getId());
        return RoChangeEntityUtils.resultRoUser(user, userImg);
    }

    @Override
    public void modifyPasswordOrdinary(String phone, String password) throws OperationException {
        this.validatePhoneByForget(phone);
        User user = this.findByPhoneUnDelete(phone);
        String salt = UserUtils.getSalt();
        String encryptPassword = UserUtils.getEncryptPassword(phone, password, salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        this.save(user);
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
    public User findByPhoneUnDelete(String phone) throws OperationException {
        Optional<User> byPhoneAndDeleteState = userRepository.findByPhoneAndDeleteState(phone, ConstantService.UN_DELETED);
        return byPhoneAndDeleteState.orElseThrow(() -> new OperationException("该用户不存在"));
    }


    @Override
    public User findByUserId(Long userId) throws OperationException {
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
    public void validatePhoneByRegister(String phone) throws OperationException {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (userOptional.isPresent()) {
            throw new OperationException("该手机号已经注册，请直接登录");
        }
    }

    @Override
    public void validatePhoneByForget(String phone) throws OperationException {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (!userOptional.isPresent()) {
            throw new OperationException("该手机号没有注册，请直接注册");
        }
    }

    private Map<String, SearchFilter> buildQueryMap(User user) {
        Map<String, SearchFilter> filters = Maps.newHashMap();
        if (StringUtils.isNotEmpty(user.getAccount())) {
            filters.put("account", new SearchFilter("account", user.getAccount(), SearchFilter.Operator.LIKE));
        }
        if (StringUtils.isNotEmpty(user.getUserName())) {
            filters.put("userName", new SearchFilter("userName", user.getUserName(), SearchFilter.Operator.LIKE));
        }
        filters.put("deleteState", new SearchFilter("deleteState", ConstantService.UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }

    private Map<String, SearchFilter> getEffectiveParams() {
        Map<String, SearchFilter> filters = Maps.newHashMap();
        filters.put("accountState", new SearchFilter("accountState", FROZEN, SearchFilter.Operator.NEQ));
        filters.put("deleteState", new SearchFilter("deleteState", ConstantService.UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }
}
