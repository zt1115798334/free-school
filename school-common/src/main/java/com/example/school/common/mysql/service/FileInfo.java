package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;
import com.example.school.common.utils.module.UploadFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:20
 * description:
 */
public interface FileInfo extends Base<com.example.school.common.mysql.entity.FileInfo, Long> {

    com.example.school.common.mysql.entity.FileInfo saveFileInfo(UploadFile uploadFile);

    List<com.example.school.common.mysql.entity.FileInfo> saveFileInfo(List<UploadFile> uploadFile);

    com.example.school.common.mysql.entity.FileInfo findFileInfo(Long id) ;

}
