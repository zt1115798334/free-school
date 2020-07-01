package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.ro.RoUser;
import com.example.school.common.base.service.Base;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 20:11
 * description:
 */
public interface Zan extends Base<com.example.school.common.mysql.entity.Zan, Long> {

    Map<Long, Long> zanState(Long userId, List<Long> topicId, Short topicType, Short zanType);

    Map<Long, Long> countZan(List<Long> topicId, Short topicType, Short zanType);

    Map<Long, List<RoUser>> zanUser(List<Long> topicId, Short topicType, Short zanType);

    void enableOnZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId);

    void enableOffZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId);
}
