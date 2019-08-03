package com.example.school.common.base.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/3 12:51
 * description:
 */
@Getter
@Setter
public class VoStorageSchoolAdministration implements Serializable {
    /**
     * 学号
     */
    @ApiModelProperty(value = "学号")
    private String studentId;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String studentPwd;
}
