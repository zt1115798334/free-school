package com.example.school.common.base.entity.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class RoSchoolAdministration implements Serializable {
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
    /**
     * 可用状态  0 可用  1 不可用
     */
    @ApiModelProperty(value = "可用状态  0 可用  1 不可用")
    private Short usableState;
}
