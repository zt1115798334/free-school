package com.example.school.common.base.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@Getter
@Setter
public class VoStorageFeedback implements Serializable {

    /**
     *
     */
    @ApiModelProperty(value = "类型")
    private Short feedbackType;
    /**
     *
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactMode;
}
