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
 * date: 2019/07/09 10:21
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_app_manage")
public class AppManage extends IdEntity {

    /**
     * android 安卓   ios 苹果
     */
    private String systemType;
    /**
     * 版本号
     */
    private String version;
    /**
     * 版本号标固
     */
    private String versionCode;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 描述
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public AppManage(String systemType, String version, String versionCode, String fileName, String filePath, String content) {
        this.systemType = systemType;
        this.version = version;
        this.versionCode = versionCode;
        this.fileName = fileName;
        this.filePath = filePath;
        this.content = content;
    }
}
