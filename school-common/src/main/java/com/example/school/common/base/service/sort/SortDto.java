package com.example.school.common.base.service.sort;

import com.example.school.common.constant.SysConst;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
@Data
@AllArgsConstructor
public class SortDto {

    //排序方式
    private String orderType;

    //排序字段
    private String orderField;

    //默认为DESC排序
    public SortDto(String orderField) {
        this.orderField = orderField;
        this.orderType = SysConst.SortOrder.DESC.getCode();
    }
}
