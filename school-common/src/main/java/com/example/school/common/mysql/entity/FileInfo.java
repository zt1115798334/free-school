package com.example.school.common.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:08
 * description:
 */
@Entity
@Table(name = "t_file_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 上传路径
     */
    private String filePath;

    /**
     * 图片压缩后的地址
     */
    private String compressFilePath;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件md5
     */
    @Column(name = "file_md5")
    private String fileMD5;
    /**
     * 存在服务器的名称
     */
    private String fullFileName;
    /**
     * 原名称 带后缀
     */
    private String originalFileName;
    /**
     * 原名称
     */
    private String fileName;
    /**
     * 后缀名
     */
    private String suffixName;

    /**
     * 创建时间
     */
    private LocalDateTime createdDatetime;

    public FileInfo(String filePath, String compressFilePath, String fileSize, String fileMD5, String fullFileName, String originalFileName, String fileName, String suffixName, LocalDateTime createdDatetime) {
        this.filePath = filePath;
        this.compressFilePath = compressFilePath;
        this.fileSize = fileSize;
        this.fileMD5 = fileMD5;
        this.fullFileName = fullFileName;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.suffixName = suffixName;
        this.createdDatetime = createdDatetime;
    }
}
