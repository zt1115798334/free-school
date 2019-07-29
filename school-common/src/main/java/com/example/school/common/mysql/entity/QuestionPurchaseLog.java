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
 * date: 2019/07/17 14:37
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_question_purchase_log")
public class QuestionPurchaseLog extends IdEntity {

	/**
	* 用户id
	*/
	private Long userId;
	/**
	* 题库表id
	*/
	private Long questionBankId;
	/**
	* 创建时间
	*/
	private LocalDateTime createdTime;

	public QuestionPurchaseLog(Long userId, Long questionBankId) {
		this.userId = userId;
		this.questionBankId = questionBankId;
	}
}
