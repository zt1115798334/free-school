package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.entity.FileInfo;
import com.example.school.common.mysql.entity.TopicImg;
import com.example.school.common.mysql.repo.TopicImgRepository;
import com.example.school.common.mysql.service.FileInfoService;
import com.example.school.common.mysql.service.TopicImgService;
import com.example.school.common.utils.FileUtils;
import com.example.school.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 15:47
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class TopicImgServiceImpl implements TopicImgService {


    private final TopicImgRepository topicImgRepository;

    private final FileInfoService fileInfoService;

    @Override
    public void deleteById(Long aLong) {
        topicImgRepository.findById(aLong).ifPresent(topicImg -> {
            topicImg.setDeleteState(DELETED);
            topicImgRepository.save(topicImg);
        });
    }

    @Override
    public void saveTopicImgFile(HttpServletRequest request, Long topicId, Short topicType) {
        String folderPath = getFolderPath();
        List<UploadFile> uploadFile = FileUtils.batchUploadFile(request, folderPath);
        List<FileInfo> fileInfoList = fileInfoService.saveFileInfo(uploadFile);
        List<TopicImg> topicImgList = fileInfoList.stream().map(FileInfo::getId)
                .map(fileInfoId ->
                        new TopicImg(topicId, topicType, fileInfoId, currentDateTime(), UN_DELETED))
                .collect(toList());
        topicImgRepository.saveAll(topicImgList);
    }

    @Override
    public Map<Long, List<Long>> findTopicImgList(List<Long> topicId, Short topicType) {
        List<TopicImg> topicImgList = topicImgRepository.findByTopicIdInAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        return topicImgList.stream().collect(groupingBy(TopicImg::getTopicId, mapping(TopicImg::getImgId, toList())));
    }

    private String getFolderPath() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(LocalDate.now());
        return System.getProperty("user.dir") + File.separator + "file" + File.separator + date + File.separator;
    }

}
