package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.FeedbackImg;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
public interface FeedbackImgRepository extends CrudRepository<FeedbackImg, Long> {

    List<FeedbackImg> findByFeedbackIdAndDeleteState(Long feedbackId, Short deleteState);

    List<FeedbackImg> findByFeedbackIdInAndDeleteState(List<Long> feedbackIds, Short deleteState);
}
