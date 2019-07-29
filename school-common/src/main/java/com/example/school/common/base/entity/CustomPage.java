package com.example.school.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/30 15:58
 * description: 分页类
 */
public class CustomPage extends IdPageEntity {

    public CustomPage(int pageNumber, int pageSize) {
        super(pageNumber, pageSize);
    }
}
