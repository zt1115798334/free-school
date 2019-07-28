package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Transaction;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 16:10
 * description:
 */
public interface TransactionService extends BaseService<Transaction, Long> {

    RoTransaction saveTransaction(Transaction transaction);

    void deleteTransaction(Long id);

    void modifyTransactionSateToNewRelease(Long id);

    void modifyTransactionSateToAfterRelease(List<Long> userId);

    RoTransaction findTransaction(Long id, Long userId) throws OperationException;

    CustomPage<RoTransaction> findTransactionEffectivePage(Transaction transaction, Long userId);

    CustomPage<RoTransaction> findTransactionUserPage(Transaction transaction, Long userId);

}
