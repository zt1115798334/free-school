package com.example.school.common.base.entity.ro;

import com.example.school.common.constant.SysConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/14 13:16
 * description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoUser implements Serializable {


    /**
     * 用户名Id
     */
    @ApiModelProperty(value = "用户名Id")
    private Long userId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 个人签名
     */
    @ApiModelProperty(value = "个人签名")
    private String personalSignature;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Short sex;
    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Long integral;
    /**
     * 学校
     */
    @ApiModelProperty(value = "学校")
    private String school;
    /**
     * 账户类型：{admin :管理员用户,studentPresident:学生会用户,student:学生用户}
     * {@link SysConst.AccountType}
     */
    @ApiModelProperty(value = "账户类型")
    private String accountType;
    /**
     * 用户头像链接
     */
    @ApiModelProperty(value = "用户头像链接")
    private RoImagePath userImgUrl;
}