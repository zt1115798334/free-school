package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_user_log")
public class UserLog extends IdEntity {

    /**
     * 操作员ID
     */
    private Long userId;
    /**
     * 操作员姓名
     */
    private String name;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作详情
     */
    private String content;
    /**
     * IP
     */
    private String ip;
    /**
     * 创建时间
     */
    private LocalDateTime time;
    /**
     * 类
     */
    private String classify;
    /**
     * 方法
     */
    private String fun;

    /**
     * 返回值
     */
    private String response;

}
