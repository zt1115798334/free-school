package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.entity.FeedbackImg;
import com.example.school.common.mysql.repo.FeedbackImgRepository;
import com.example.school.common.mysql.service.FeedbackImgService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
public class FeedbackImgServiceImpl implements FeedbackImgService {


    private final FeedbackImgRepository feedbackImgRepository;

    @Override
    public Iterable<FeedbackImg> saveAll(Iterable<FeedbackImg> t) {
        return feedbackImgRepository.saveAll(t);
    }

    @Override
    public Map<Long, List<Long>> findByFeedbackId(Long feedbackId) {
        List<FeedbackImg> feedbackImgList = feedbackImgRepository.findByFeedbackIdAndDeleteState(feedbackId, UN_DELETED);
        return feedbackImgMap(feedbackImgList);
    }

    @Override
    public Map<Long, List<Long>> findByFeedbackIds(List<Long> feedbackIds) {
        List<FeedbackImg> feedbackImgList = feedbackImgRepository.findByFeedbackIdInAndDeleteState(feedbackIds, UN_DELETED);
        return feedbackImgMap(feedbackImgList);
    }

    private Map<Long, List<Long>> feedbackImgMap(List<FeedbackImg> feedbackImgList) {
        return feedbackImgList.stream()
                .collect(groupingBy(FeedbackImg::getFeedbackId, mapping(FeedbackImg::getImgId, toList())));
    }
}
