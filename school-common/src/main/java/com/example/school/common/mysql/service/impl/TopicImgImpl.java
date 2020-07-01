package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.TopicImgRepository;
import com.example.school.common.mysql.service.FileInfo;
import com.example.school.common.mysql.service.TopicImg;
import com.example.school.common.utils.FileUtils;
import com.example.school.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
public class TopicImgImpl implements TopicImg {

    private final TopicImgRepository topicImgRepository;

    private final FileInfo fileInfoService;

    @Override
    public void deleteById(Long aLong) {
        topicImgRepository.findById(aLong).ifPresent(topicImg -> {
            topicImg.setDeleteState(DELETED);
            topicImgRepository.save(topicImg);
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveTopicImgFile(HttpServletRequest request, Long topicId, Short topicType) {
        String folderPath = FileUtils.getFolderPath(FOLDER_IMG);
        List<UploadFile> uploadFile = FileUtils.batchUploadFile(request, folderPath);
        List<com.example.school.common.mysql.entity.FileInfo> fileInfoList = fileInfoService.saveFileInfo(uploadFile);
        List<com.example.school.common.mysql.entity.TopicImg> topicImgList = fileInfoList.stream().map(com.example.school.common.mysql.entity.FileInfo::getId)
                .map(fileInfoId ->
                        new com.example.school.common.mysql.entity.TopicImg(topicId, topicType, fileInfoId, currentDateTime(), UN_DELETED))
                .collect(toList());
        topicImgRepository.saveAll(topicImgList);
    }

    @Override
    public Map<Long, List<Long>> findTopicImgList(List<Long> topicId, Short topicType) {
        List<com.example.school.common.mysql.entity.TopicImg> topicImgList = topicImgRepository.findByTopicIdInAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        return topicImgList.stream().collect(groupingBy(com.example.school.common.mysql.entity.TopicImg::getTopicId, mapping(com.example.school.common.mysql.entity.TopicImg::getImgId, toList())));
    }

}
