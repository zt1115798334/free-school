package com.example.school.common.utils.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/20 14:44
 * description:
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 压缩图片文件路径
     */
    private String compressFilePath;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 文件md5
     */
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

}
