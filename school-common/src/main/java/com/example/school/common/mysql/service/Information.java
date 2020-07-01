package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoInformation;
import com.example.school.common.base.service.Base;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 15:11
 * description:
 */
public interface Information extends Base<com.example.school.common.mysql.entity.Information, Long> {
    RoInformation saveInformation(com.example.school.common.mysql.entity.Information information);

    void deleteInformation(Long id);

    void modifyInformationSateToNewRelease(Long id);

    void modifyInformationSateToAfterRelease(List<Long> userId);

    void incrementInformationBrowsingVolume(Long id);

    com.example.school.common.mysql.entity.Information findInformation(Long id) ;

    RoInformation findRoInformation(Long id, Long userId) ;

    PageImpl<RoInformation> findInformationEffectivePage(com.example.school.common.mysql.entity.Information information, Long userId);

    PageImpl<RoInformation> findInformationUserPage(com.example.school.common.mysql.entity.Information information, Long userId);

    PageImpl<RoInformation> findInformationCollectionPage(CustomPage customPage, Long userId);

}
