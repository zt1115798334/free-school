package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdPageEntity;
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
 * date: 2019/07/17 14:37
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_question_bank")
public class QuestionBank extends IdPageEntity {

	/**
	* 用户id
	*/
	private Long userId;
	/**
	* 标题
	*/
	private String title;
	/**
	* 描述
	*/
	private String describeContent;
	/**
	* 积分
	*/
	private Long integral;
	/**
	* 不存在答案的
	*/
	private Long nonExistentAnswer;
	/**
	* 存在答案的
	*/
	private Long existenceAnswer;
	/**
	 * 浏览量
	 */
	private Long browsingVolume;
	/**
	* 交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决,lowerShelf，下架}
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

	public QuestionBank(Long id, String title, String describeContent,Long integral) {
		this.id = id;
		this.title = title;
		this.describeContent = describeContent;
		this.integral = integral;
	}

	public QuestionBank(String sortName, String sortOrder, int pageNumber, int pageSize, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		super(sortName, sortOrder, pageNumber, pageSize, startDateTime, endDateTime);
	}

}
