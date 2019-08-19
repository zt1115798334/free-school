package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:29
 * description:
 */
public interface UserRepository extends CrudRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    Optional<User> findByIdAndDeleteState(Long id, Short deleteState);

    Optional<User> findByAccountAndDeleteState(String account, Short deleteState);

    Optional<User> findByPhoneAndDeleteState(String phone, Short deleteState);

    List<User> findByIdInAndDeleteState(List<Long> id, Short deleteState);

}
