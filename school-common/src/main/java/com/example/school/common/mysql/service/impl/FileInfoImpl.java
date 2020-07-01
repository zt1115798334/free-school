package com.example.school.common.mysql.service.impl;

import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.repo.FileInfoRepository;
import com.example.school.common.mysql.service.FileInfo;
import com.example.school.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:22
 * description:
 */
@AllArgsConstructor
@Service
public class FileInfoImpl implements FileInfo {

    private final FileInfoRepository fileInfoRepository;

    @Override
    public void deleteById(Long id) {
        fileInfoRepository.deleteById(id);
    }

    @Override
    public Optional<com.example.school.common.mysql.entity.FileInfo> findById(Long id) {
        return fileInfoRepository.findById(id);
    }

    @Override
    public List<com.example.school.common.mysql.entity.FileInfo> findByIds(List<Long> id) {
        return (List<com.example.school.common.mysql.entity.FileInfo>) fileInfoRepository.findAllById(id);
    }

    @Override
    public List<com.example.school.common.mysql.entity.FileInfo> findAll() {
        return (List<com.example.school.common.mysql.entity.FileInfo>) fileInfoRepository.findAll();
    }

    @Override
    public com.example.school.common.mysql.entity.FileInfo saveFileInfo(UploadFile uploadFile) {
        return fileInfoRepository.save(getUploadConvertFileInfos((uploadFile)));
    }

    @Override
    public List<com.example.school.common.mysql.entity.FileInfo> saveFileInfo(List<UploadFile> uploadFile) {
        return (List<com.example.school.common.mysql.entity.FileInfo>) fileInfoRepository.saveAll(getUploadConvertFileInfos(uploadFile));
    }

    @Override
    public com.example.school.common.mysql.entity.FileInfo findFileInfo(Long id) {
        Optional<com.example.school.common.mysql.entity.FileInfo> infoOptional = findById(id);
        return infoOptional.orElseThrow(() -> new OperationException("图片不存在"));

    }

    private com.example.school.common.mysql.entity.FileInfo getUploadConvertFileInfos(UploadFile uploadFile) {
        return new com.example.school.common.mysql.entity.FileInfo(
                uploadFile.getFilePath(),
                uploadFile.getCompressFilePath(),
                uploadFile.getFileSize(),
                uploadFile.getFileMD5(),
                uploadFile.getFullFileName(),
                uploadFile.getOriginalFileName(),
                uploadFile.getFileName(),
                uploadFile.getSuffixName(),
                LocalDateTime.now());
    }

    private List<com.example.school.common.mysql.entity.FileInfo> getUploadConvertFileInfos(List<UploadFile> files) {
        return files.parallelStream().map(this::getUploadConvertFileInfos).collect(Collectors.toList());
    }
}
