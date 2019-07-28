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
public class VoCommentPage extends VoPage {
    /**
     * 主体id
     */
    @ApiModelProperty(value = "主体id")
    private Long topicId;
}
