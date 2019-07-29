package com.example.school.common.base.entity.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoRecordTime extends RoTopic {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    private String dateTime;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String describeContent;

    public RoRecordTime(RoUser user, String state, boolean userState, boolean zanState, Long zanNum, List<RoUser> zanUsers, boolean collectionState, Long commentNum, Long browsingVolume, List<RoImagePath> topicImgList,
                        Long id, String title, String dateTime, String describeContent) {
        super(user, state, userState, zanState, zanNum, zanUsers, collectionState, commentNum, browsingVolume, topicImgList);
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.describeContent = describeContent;
    }
}
