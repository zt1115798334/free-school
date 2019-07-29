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
 * date: 2019/07/19 11:06
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_collection")
public class Collection extends IdPageEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
    /**
     * 主体id
     */
    private Long topicId;
    /**
     * 主体类型：1，交易；2，讯息；3，问答；4，题库；5，时光
     */
    private Short topicType;
    /**
     * 收藏状态 0 取消收藏 1 开启收藏
     */
    private Short collectionState;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    public Collection(Long userId,Long topicId, Short topicType, Short collectionState) {
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
        this.collectionState = collectionState;
    }
}
