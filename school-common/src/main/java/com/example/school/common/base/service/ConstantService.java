package com.example.school.common.base.service;


import com.example.school.common.constant.SysConst;
import com.example.school.common.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface ConstantService {

    /**
     * 未删除
     */
    Short UN_DELETED = SysConst.DeleteState.UN_DELETED.getCode();

    /**
     * 删除
     */
    Short DELETED = SysConst.DeleteState.DELETE.getCode();

    /**
     * 隐藏
     */
    Short HIDE = SysConst.ShowState.HIDE.getCode();

    /**
     * 显示
     */
    Short DISPLAY = SysConst.ShowState.DISPLAY.getCode();

    /**
     * 关
     */
    short OFF = SysConst.EnabledState.OFF.getCode();

    /**
     * 开
     */
    short ON = SysConst.EnabledState.ON.getCode();

    /**
     * 正序
     */
    String ASC = SysConst.SortOrder.ASC.getCode();

    /**
     * 倒叙
     */
    String DESC = SysConst.SortOrder.DESC.getCode();

    /**
     * 发布中
     */
    String IN_RELEASE = SysConst.State.IN_RELEASE.getType();
    /**
     * 新发布
     */
    String NEW_RELEASE = SysConst.State.NEW_RELEASE.getType();
    /**
     * 发布后
     */
    String AFTER_RELEASE = SysConst.State.AFTER_RELEASE.getType();
    /**
     * 已解决
     */
    String SOLVE = SysConst.State.SOLVE.getType();
    /**
     * 下架
     */
    String LOWER_SHELF = SysConst.State.LOWER_SHELF.getType();

    Short TOPIC_TYPE_1 = SysConst.TopicType.TOPIC_TYPE_1.getCode();
    Short TOPIC_TYPE_2 = SysConst.TopicType.TOPIC_TYPE_2.getCode();
    Short TOPIC_TYPE_3 = SysConst.TopicType.TOPIC_TYPE_3.getCode();
    Short TOPIC_TYPE_4 = SysConst.TopicType.TOPIC_TYPE_4.getCode();
    Short TOPIC_TYPE_5 = SysConst.TopicType.TOPIC_TYPE_5.getCode();

    Short ZAN_TOPIC = SysConst.ZanType.ZAN_TOPIC.getCode();
    Short ZAN_COMMENT = SysConst.ZanType.ZAN_COMMENT.getCode();

    String FOLDER_IMG = SysConst.FolderType.FOLDER_IMG.getType();
    String FOLDER_HEAD_PORTRAIT = SysConst.FolderType.FOLDER_HEAD_PORTRAIT.getType();
    String FOLDER_FEEDBACK_IMG = SysConst.FolderType.FOLDER_FEEDBACK_IMG.getType();
    String FOLDER_QUESTION_BANK_FILE = SysConst.FolderType.FOLDER_QUESTION_BANK_FILE.getType();

    /**
     * 排序状态
     */
    String DEFAULT_SORT_NAME = SysConst.DEFAULT_SORT_NAME;

    default LocalDate currentDate() {
        return DateUtils.currentDate();
    }

    default LocalDateTime currentDateTime() {
        return DateUtils.currentDateTime();
    }

}
