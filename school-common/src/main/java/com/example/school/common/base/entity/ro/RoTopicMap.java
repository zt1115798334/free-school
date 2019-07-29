package com.example.school.common.base.entity.ro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoTopicMap{

    private Map<Long, RoUser> userMap;
    private Map<Long, List<Long>> topicImgMap;
    private Map<Long, Long> zanNumMap;
    private Map<Long, Long> zanStateMap;
    private Map<Long, Long> collectionStateMap;
    private Map<Long, Long> commentCountMap;
    private Map<Long, List<RoUser>> zanUserMap;

}
