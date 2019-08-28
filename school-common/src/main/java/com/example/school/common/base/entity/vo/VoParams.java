package com.example.school.common.base.entity.vo;

import com.example.school.common.utils.RegularMatchUtils;
import com.example.school.common.validation.SearchArea;
import com.example.school.common.validation.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:48
 * description:条件查询
 */
@ApiModel(description = "条件查询")
@Getter
@Setter
public class VoParams extends VoPage {
    /**
     * 时间类型 all>>,全部today>>今天,yesterday>>昨天,week>>近一周,month>>近一个月,define>>自定义
     */
    @ApiModelProperty(value = "时间类型 all>>,全部today>>今天,yesterday>>昨天,week>>近一周,month>>近一个月,define>>自定义 ")
    @NotBlank(message = "时间类型不能为空")
    @TimeType
    private String timeType;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间 时间格式：yyyy-MM-dd HH:mm:ss")
    @Pattern(regexp = RegularMatchUtils.YYYY_MM_DD_HH_MM_SS, message = "开始时间格式错误，时间格式：yyyy-MM-dd HH:mm:ss")
    private String startDateTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间 时间格式：yyyy-MM-dd HH:mm:ss")
    @Pattern(regexp = RegularMatchUtils.YYYY_MM_DD_HH_MM_SS, message = "结束时间格式错误，时间格式：yyyy-MM-dd HH:mm:ss")
    private String endDateTime;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字")
    @NotEmpty(message = "搜索关键字不能为空")
    @Length(max = 38, message = "搜索关键词长度最大为38个字符")
    private String searchValue;
    /**
     * 搜索范围 all>>全部,author>>作者,title>>标题,content>>内容
     */
    @ApiModelProperty(value = "搜索范围 all>>全部,author>>作者,title>>标题,content>>内容")
    @SearchArea
    private String searchArea;
}
