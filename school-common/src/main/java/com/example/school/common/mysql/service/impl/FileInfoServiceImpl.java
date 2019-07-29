package com.example.school.common.mysql.service.impl;

import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.FileInfo;
import com.example.school.common.mysql.repo.FileInfoRepository;
import com.example.school.common.mysql.service.FileInfoService;
import com.example.school.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
public class FileInfoServiceImpl implements FileInfoService {

    private final FileInfoRepository fileInfoRepository;

    @Override
    public void deleteById(Long id) {
        fileInfoRepository.deleteById(id);
    }

    @Override
    public Optional<FileInfo> findById(Long id) {
        return fileInfoRepository.findById(id);
    }

    @Override
    public List<FileInfo> findByIds(List<Long> id) {
        return (List<FileInfo>) fileInfoRepository.findAllById(id);
    }

    @Override
    public List<FileInfo> findAll() {
        return (List<FileInfo>) fileInfoRepository.findAll();
    }

    @Override
    public FileInfo saveFileInfo(UploadFile uploadFile) {
        return fileInfoRepository.save(getUploadConvertFileInfos((uploadFile)));
    }

    @Override
    public List<FileInfo> saveFileInfo(List<UploadFile> uploadFile) {
        return (List<FileInfo>) fileInfoRepository.saveAll(getUploadConvertFileInfos(uploadFile));
    }

    @Override
    public FileInfo findFileInfo(Long id) {
        Optional<FileInfo> infoOptional = findById(id);
        return infoOptional.orElseThrow(() -> new OperationException("图片不存在"));

    }

    private FileInfo getUploadConvertFileInfos(UploadFile uploadFile) {
        return new FileInfo(
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

    private List<FileInfo> getUploadConvertFileInfos(List<UploadFile> files) {
        return files.parallelStream().map(this::getUploadConvertFileInfos).collect(Collectors.toList());
    }
}
