package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.FeedbackImgRepository;
import com.example.school.common.mysql.service.FeedbackImg;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@AllArgsConstructor
@Service
public class FeedbackImgImpl implements FeedbackImg {


    private final FeedbackImgRepository feedbackImgRepository;

    @Override
    public Iterable<com.example.school.common.mysql.entity.FeedbackImg> saveAll(Iterable<com.example.school.common.mysql.entity.FeedbackImg> t) {
        return feedbackImgRepository.saveAll(t);
    }

    @Override
    public Map<Long, List<Long>> findByFeedbackId(Long feedbackId) {
        List<com.example.school.common.mysql.entity.FeedbackImg> feedbackImgList = feedbackImgRepository.findByFeedbackIdAndDeleteState(feedbackId, UN_DELETED);
        return feedbackImgMap(feedbackImgList);
    }

    @Override
    public Map<Long, List<Long>> findByFeedbackIds(List<Long> feedbackIds) {
        List<com.example.school.common.mysql.entity.FeedbackImg> feedbackImgList = feedbackImgRepository.findByFeedbackIdInAndDeleteState(feedbackIds, UN_DELETED);
        return feedbackImgMap(feedbackImgList);
    }

    private Map<Long, List<Long>> feedbackImgMap(List<com.example.school.common.mysql.entity.FeedbackImg> feedbackImgList) {
        return feedbackImgList.stream()
                .collect(groupingBy(com.example.school.common.mysql.entity.FeedbackImg::getFeedbackId, mapping(com.example.school.common.mysql.entity.FeedbackImg::getImgId, toList())));
    }
}
