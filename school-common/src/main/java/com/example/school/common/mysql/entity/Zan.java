package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 20:11
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_zan")
public class Zan extends IdEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 主体id或者评论的id
     */
    private Long topicId;
    /**
     * 主体类型：1，交易；2，讯息；3，问答；4，题库；5，时光
     */
    private Short topicType;
    /**
     * 点赞类型  1主体点赞  2 主体评论点赞。
     */
    private Short zanType;
    /**
     * 点赞状态  0--取消赞   1--有效赞
     */
    private Short zanState;


}
