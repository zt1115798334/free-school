package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
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
 * date: 2019/03/20 09:37
 * description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_verification_code_log")
public class VerificationCodeLog extends IdEntity {

    /**
     * 通知内容
     */
    private String noticeContent;
    /**
     * 通知类型
     */
    private String noticeType;
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码类型
     */
    private String codeType;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
