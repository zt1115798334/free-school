package com.example.school.common.base.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/20 9:02
 * description:
 */
@Getter
@Setter
public class VoCommentReplyPage extends VoPage {
    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;
    /**
     * 1为回复评论，2为回复别人的回复
     */
    @ApiModelProperty(value = "评论id")
    private Short replyType;
    /**
     * 回复目标id，reply_type为1时，是comment_id，reply_type为2时为回复表的id
     */
    @ApiModelProperty(value = "评论id")
    private Long replyId;

}
