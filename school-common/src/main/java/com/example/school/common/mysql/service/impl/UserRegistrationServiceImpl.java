package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.entity.UserRegistration;
import com.example.school.common.mysql.repo.UserRegistrationRepository;
import com.example.school.common.mysql.service.UserRegistrationService;
import com.example.school.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/09/25 11:36
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;

    @Override
    public UserRegistration save(UserRegistration userRegistration) {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Optional<UserRegistration> repository = userRegistrationRepository.findByUserIdAndRegistrationId(userRegistration.getUserId(), userRegistration.getRegistrationId());
        if (repository.isPresent()) {
            UserRegistration registration = repository.get();
            registration.setTime(currentDateTime);
            registration.setToken(userRegistration.getToken());
            userRegistration = userRegistrationRepository.save(registration);
        } else {
            userRegistration.setTime(currentDateTime);
            userRegistration = userRegistrationRepository.save(userRegistration);
        }
        return userRegistration;
    }

    @Override
    public void deleteByUserIdAndRegistrationId(Long userId, String registrationId) {
        userRegistrationRepository.deleteByUserIdAndRegistrationId(userId, registrationId);
    }

    @Override
    public List<String> findRegistrationIdByUserId(Long userId) {
        return userRegistrationRepository.queryRegistrationIdByUserId(userId);
    }
}
