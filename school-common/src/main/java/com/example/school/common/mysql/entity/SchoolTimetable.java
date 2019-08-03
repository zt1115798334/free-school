package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;


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
@Table(name = "t_school_timetable")
public class SchoolTimetable extends IdEntity {

    /**
     * 学号
     */
    private String studentId;
    /**
     * 学期
     */
    private String semester;
    /**
     * 周次
     */
    private Integer weeklyTimes;
    /**
     * 周（星期一 mon 星期二 tue 星期三 wed 星期四 thu 星期五 fri 星期六 sat 星期天 sun）
     * {@link com.example.school.common.constant.SysConst.Week}
     */
    private String week;
    /**
     * 课时（第一节大课 1，第二节大课 2, 第三节大课 3,第四节大课 4 ， 第五节大课 5， 第六节大课 6）
     * {@link com.example.school.common.constant.SysConst.ClassTimes}
     */
    private Short classTimes;

    /**
     * 课程
     */
    private String curriculum;
}
