package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/09/25 11:36
 * description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "t_user_registration")
public class UserRegistration extends IdEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 极光推送id
     */
    private String registrationId;
    /**
     * 用户的token
     */
    private String token;
    /**
     * 创建时间
     */
    private LocalDateTime time;

    public UserRegistration(Long userId, String registrationId, String token) {
        this.userId = userId;
        this.registrationId = registrationId;
        this.token = token;
    }
}
