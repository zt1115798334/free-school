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
 * date: 2019/06/24 10:16
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_img")
public class UserImg extends IdEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 图片id
     */
    private Long imgId;
    /**
     * 状态：1  开启 0 停用
     */
    private Short state;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public UserImg(Long userId, Long imgId) {
        this.userId = userId;
        this.imgId = imgId;
    }
}
