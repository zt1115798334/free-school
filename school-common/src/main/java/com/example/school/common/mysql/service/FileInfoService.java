package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.FileInfo;
import com.example.school.common.utils.module.UploadFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:20
 * description:
 */
public interface FileInfoService extends Base<FileInfo, Long> {

    FileInfo saveFileInfo(UploadFile uploadFile);

    List<FileInfo> saveFileInfo(List<UploadFile> uploadFile);

    FileInfo findFileInfo(Long id) ;

}
