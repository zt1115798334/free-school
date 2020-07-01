package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoKnowing;
import com.example.school.common.base.service.Base;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 11:34
 * description:
 */
public interface Knowing extends Base<com.example.school.common.mysql.entity.Knowing, Long> {
    RoKnowing saveKnowing(com.example.school.common.mysql.entity.Knowing knowing, Long userId) ;

    void deleteKnowing(Long id) ;

    void modifyKnowingSateToNewRelease(Long id);

    void modifyKnowingSateToAfterRelease(List<Long> userId);

    void adoptKnowingComment(Long id, Long userId, Long commentId, Long commentUserId) ;

    void incrementKnowingBrowsingVolume(Long id);

    com.example.school.common.mysql.entity.Knowing findKnowing(Long id) ;

    RoKnowing findRoKnowing(Long id, Long userId) ;

    PageImpl<RoKnowing> findKnowingEffectivePage(com.example.school.common.mysql.entity.Knowing knowing, Long userId);

    PageImpl<RoKnowing> findKnowingUserPage(com.example.school.common.mysql.entity.Knowing knowing, Long userId);

    PageImpl<RoKnowing> findKnowingCollectionPage(CustomPage customPage, Long userId);

}
