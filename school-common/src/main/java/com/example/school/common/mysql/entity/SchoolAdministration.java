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
 * date: 2019/08/03 12:30
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_school_administration")
public class SchoolAdministration extends IdEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 学校标识
     */
    private Short schoolCode;

    /**
     * 学号
     */
    private String studentId;
    /**
     * 密码
     */
    private String studentPwd;
    /**
     * 可用状态  0 可用  1 不可用
     */
    private Short usableState;
    /**
     * 新状态 0 新  1 旧
     */
    private Short freshState;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;

    public SchoolAdministration(Long userId, Short schoolCode,  String studentId, String studentPwd) {
        this.userId = userId;
        this.schoolCode = schoolCode;
        this.studentId = studentId;
        this.studentPwd = studentPwd;
    }
}
