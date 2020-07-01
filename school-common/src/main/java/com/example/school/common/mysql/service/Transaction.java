package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.service.Base;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 16:10
 * description:
 */
public interface Transaction extends Base<com.example.school.common.mysql.entity.Transaction, Long> {

    RoTransaction saveTransaction(com.example.school.common.mysql.entity.Transaction transaction);

    void deleteTransaction(Long id);

    void modifyTransactionSateToNewRelease(Long id);

    void modifyTransactionSateToAfterRelease(List<Long> userId);

    void incrementTransactionBrowsingVolume(Long id);

    com.example.school.common.mysql.entity.Transaction findTransaction(Long id) ;

    RoTransaction findRoTransaction(Long id, Long userId) ;

    PageImpl<RoTransaction> findTransactionEffectivePage(com.example.school.common.mysql.entity.Transaction transaction, Long userId);

    PageImpl<RoTransaction> findTransactionUserPage(com.example.school.common.mysql.entity.Transaction transaction, Long userId);

    PageImpl<RoTransaction> findTransactionCollectionPage(CustomPage customPage, Long userId);

}
