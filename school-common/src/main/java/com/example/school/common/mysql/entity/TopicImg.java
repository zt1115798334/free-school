package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 15:47
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_topic_img")
public class TopicImg extends IdEntity {
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
	* 图片id
	*/
	private Long imgId;
	/**
	* 创建时间
	*/
	private LocalDateTime createdTime;
	/**
	* 删除状态：1已删除 0未删除
	*/
	private Short deleteState;

}
