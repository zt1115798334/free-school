package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.ro.RoFeedback;
import com.example.school.common.mysql.repo.FeedbackRepository;
import com.example.school.common.mysql.service.FeedbackImg;
import com.example.school.common.mysql.service.Feedback;
import com.example.school.common.mysql.service.FileInfo;
import com.example.school.common.utils.FileUtils;
import com.example.school.common.utils.change.RoChangeEntityUtils;
import com.example.school.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@AllArgsConstructor
@Service
public class FeedbackImpl implements Feedback {


    private final FeedbackRepository feedbackRepository;

    private final FeedbackImg feedbackImgService;

    private final FileInfo fileInfoService;

    @Override
    public com.example.school.common.mysql.entity.Feedback save(com.example.school.common.mysql.entity.Feedback feedback) {
        feedback.setDeleteState(UN_DELETED);
        feedback.setCreatedTime(currentDateTime());
        return feedbackRepository.save(feedback);
    }

    @Override
    public RoFeedback saveFeedback(com.example.school.common.mysql.entity.Feedback feedback) {
        feedback = this.save(feedback);
        return resultRoFeedback(feedback);
    }

    @Override
    public void saveFeedbackImg(HttpServletRequest request, Long feedbackId) {
        String folderPath = FileUtils.getFolderPath(FOLDER_FEEDBACK_IMG);
        List<UploadFile> uploadFile = FileUtils.batchUploadFile(request, folderPath);
        List<com.example.school.common.mysql.entity.FileInfo> fileInfoList = fileInfoService.saveFileInfo(uploadFile);
        List<com.example.school.common.mysql.entity.FeedbackImg> feedbackImgList = fileInfoList.stream().map(com.example.school.common.mysql.entity.FileInfo::getId)
                .map(fileInfoId ->
                        new com.example.school.common.mysql.entity.FeedbackImg(feedbackId, fileInfoId, currentDateTime(), UN_DELETED))
                .collect(toList());
        feedbackImgService.saveAll(feedbackImgList);
    }

    private RoFeedback resultRoFeedback(com.example.school.common.mysql.entity.Feedback feedback) {
        Map<Long, List<Long>> feedbackImgMap = feedbackImgService.findByFeedbackId(feedback.getId());
        return RoChangeEntityUtils.resultRoFeedback(feedback, feedbackImgMap);
    }
}
