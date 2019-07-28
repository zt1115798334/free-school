package com.example.school.common.base.service.sort;

import com.example.school.common.constant.SysConst;
import org.springframework.data.domain.Sort;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
public class SortUtils {

    public static Sort basicSort() {
        return basicSort(SysConst.SortOrder.DESC.getCode(), "id");
    }

    public static Sort basicSort(String orderType, String orderField) {
        return new Sort(Sort.Direction.fromString(orderType), orderField);
    }

    public static Sort basicSort(SortDto... dtos) {
        Sort result = null;
        for (SortDto dto : dtos) {
            if (result == null) {
                result = new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }
}
