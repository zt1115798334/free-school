package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_sign_record")
public class SignRecord extends IdEntity {

    /**
     * 索引，用户表的id
     */
    private Long userId;
    /**
     * 索引，月份，形如2019-02
     */
    private LocalDate dateMonth;
    /**
     * 用户签到的数据
     */
    private String mask;
    /**
     * 用户本月连续签到的天数
     */
    private Integer continueSignMonth;


}
