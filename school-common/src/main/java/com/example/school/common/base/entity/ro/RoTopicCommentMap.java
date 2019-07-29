package com.example.school.common.base.entity.ro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoTopicCommentMap {

    private Map<Long, RoUser> userMap;
    private Map<Long, Long> zanNumMap;
    private Map<Long, Long> zanStateMap;

}
