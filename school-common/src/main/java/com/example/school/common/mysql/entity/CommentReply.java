package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdPageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;


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
@Table(name = "t_comment_reply")
public class CommentReply extends IdPageEntity {

    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 1为回复评论，2为回复别人的回复
     */
    private Short replyType;
    /**
     * 回复目标id，reply_type为1时，是comment_id，reply_type为2时为回复表的id
     */
    private Long replyId;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复目标id
     */
    private Long toUserId;
    /**
     * 回复用户id
     */
    private Long fromUserId;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public CommentReply(Long commentId, Short replyType, Long replyId, String content, Long toUserId, Long fromUserId) {
        this.commentId = commentId;
        this.replyType = replyType;
        this.replyId = replyId;
        this.content = content;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }

    public CommentReply(String sortName, String sortOrder, int pageNumber, int pageSize, Long commentId) {
        super(sortName, sortOrder, pageNumber, pageSize);
        this.commentId = commentId;
    }
}
