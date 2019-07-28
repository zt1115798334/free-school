package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.TopicImg;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 15:47
 * description:
 */
public interface TopicImgRepository extends CrudRepository<TopicImg, Long> {

    List<TopicImg> findByTopicIdInAndTopicTypeAndDeleteState(List<Long> topicId, Short topicType, Short deleteState);
}
