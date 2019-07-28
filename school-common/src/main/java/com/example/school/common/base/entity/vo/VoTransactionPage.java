package com.example.school.common.base.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
public class VoTransactionPage implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String desc;
}
