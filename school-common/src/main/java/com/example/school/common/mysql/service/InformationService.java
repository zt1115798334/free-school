package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoInformation;
import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.Information;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 15:11
 * description:
 */
public interface InformationService extends Base<Information, Long> {
    RoInformation saveInformation(Information information);

    void deleteInformation(Long id);

    void modifyInformationSateToNewRelease(Long id);

    void modifyInformationSateToAfterRelease(List<Long> userId);

    void incrementInformationBrowsingVolume(Long id);

    Information findInformation(Long id) ;

    RoInformation findRoInformation(Long id, Long userId) ;

    PageImpl<RoInformation> findInformationEffectivePage(Information information, Long userId);

    PageImpl<RoInformation> findInformationUserPage(Information information, Long userId);

    PageImpl<RoInformation> findInformationCollectionPage(CustomPage customPage, Long userId);

}
