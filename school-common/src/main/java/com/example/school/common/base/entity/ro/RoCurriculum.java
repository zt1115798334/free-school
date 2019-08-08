package com.example.school.common.base.entity.ro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/7 17:42
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoCurriculum {

    private String curriculum;
    private String weeklyTimesCurr;
    private String classTimesCurr;
    private String teacherCurr;
    private String classroomCurr;
}
