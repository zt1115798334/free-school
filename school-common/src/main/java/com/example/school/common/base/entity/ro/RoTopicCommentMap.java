package com.example.school.common.base.entity.ro;

import com.example.school.common.mysql.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoTopicCommentMap {

    private Page<Comment> commentPage;
    private Map<Long, RoUser> roUserMap;
    private Map<Long, Long> zanNumMap;
    private Map<Long, Long> zanStateMap;

}
