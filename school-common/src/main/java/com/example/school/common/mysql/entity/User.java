package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdPageEntity;
import com.example.school.common.constant.SysConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/14 13:16
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user")
public class User extends IdPageEntity {

    /**
     * 账户
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 个人签名
     */
    private String personalSignature;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 账户状态：{0：冻结,1:正常}
     * {@link SysConst.AccountState}
     */
    private Integer accountState;

    /**
     * 账户类型：{admin :管理员用户,ordinary:普通用户}
     * {@link SysConst.AccountType}
     */
    private String accountType;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public User(String account, String password, String salt, String phone, Integer accountState, String accountType) {
        this.account = account;
        this.password = password;
        this.salt = salt;
        this.phone = phone;
        this.accountState = accountState;
        this.accountType = accountType;
    }

    public User(String userName, String personalSignature, String phone, String email) {
        this.userName = userName;
        this.personalSignature = personalSignature;
        this.phone = phone;
        this.email = email;
    }
}