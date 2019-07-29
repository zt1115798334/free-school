package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdPageEntity;
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
 * date: 2019/07/12 15:14
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_feedback")
public class Feedback extends IdPageEntity {


    /**
     * 用户id
     */
    private Long userId;
    /**
     *
     */
    private Short feedbackType;
    /**
     *
     */
    private String content;
    /**
     * 联系方式
     */
    private String contactMode;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public Feedback(Short feedbackType, String content, String contactMode) {
        this.feedbackType = feedbackType;
        this.content = content;
        this.contactMode = contactMode;
    }
}
