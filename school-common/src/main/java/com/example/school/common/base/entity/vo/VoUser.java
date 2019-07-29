package com.example.school.common.base.entity.vo;

import com.example.school.common.validation.AccountType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:48
 * description:条件查询
 */
@ApiModel(description = "用户查询")
@Getter
@Setter
public class VoUser extends VoPage {

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "账户类型")
    @AccountType
    private String accountType;

    @ApiModelProperty(value = "用户名")
    private String userName;
}
