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
 * date: 2019/06/19 18:44
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_comment")
public class Comment extends IdPageEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 主体id
     */
    private Long topicId;
    /**
     * 主体类型：1，交易；2，讯息；3，问答；4，题库；5，时光
     * {@link com.example.school.common.constant.SysConst.TopicType}
     */
    private Short topicType;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 状态
     * {@link com.example.school.common.constant.SysConst.CommentState}
     */
    private String state;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public Comment(Long userId, Long topicId, Short topicType) {
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
    }

    public Comment(String sortName, String sortOrder, int pageNumber, int pageSize, Long topicId) {
        super(sortName, sortOrder, pageNumber, pageSize);
        this.topicId = topicId;
    }

    public Comment(Long userId, Long topicId, Short topicType, String content) {
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
        this.content = content;
    }
}
