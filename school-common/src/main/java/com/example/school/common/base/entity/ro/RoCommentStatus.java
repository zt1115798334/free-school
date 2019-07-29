package com.example.school.common.base.entity.ro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoCommentStatus extends RoTopic {

    Long commentId;
    /**
     * 主体id
     */
    private Long topicId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createdTime;

    List<RoCommentReplyStatus> roCommentReplyStatusList;

    public RoCommentStatus(RoUser user, String state, boolean userState, boolean zanState, Long zanNum, Long commentId, Long topicId, String content, String createdTime) {
        super(user, state, userState, zanState, zanNum);
        this.commentId = commentId;
        this.topicId = topicId;
        this.content = content;
        this.createdTime = createdTime;
    }

    public RoCommentStatus(RoUser user, String state, boolean userState, boolean zanState, Long zanNum, Long commentId, Long topicId, String content, String createdTime, List<RoCommentReplyStatus> roCommentReplyStatusList) {
        super(user, state, userState, zanState, zanNum);
        this.commentId = commentId;
        this.topicId = topicId;
        this.content = content;
        this.createdTime = createdTime;
        this.roCommentReplyStatusList = roCommentReplyStatusList;
    }
}
