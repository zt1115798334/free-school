package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.UserRegistration;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/09/25 11:36
 * description:
 */
public interface UserRegistrationRepository extends CrudRepository<UserRegistration, Long>,
        JpaSpecificationExecutor<UserRegistration> {

    Optional<UserRegistration> findByUserIdAndRegistrationId(Long userId, String registrationId);

    @Query(value = "select registrationId from UserRegistration where userId =:userId")
    List<String> queryRegistrationIdByUserId(Long userId);

}
