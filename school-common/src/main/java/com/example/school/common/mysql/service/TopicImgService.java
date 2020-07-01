package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.TopicImg;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 15:47
 * description:
 */
public interface TopicImgService extends Base<TopicImg, Long> {

    void saveTopicImgFile(HttpServletRequest request, Long topicId, Short topicType);

    Map<Long, List<Long>> findTopicImgList(List<Long> topicId, Short topicType);

}
