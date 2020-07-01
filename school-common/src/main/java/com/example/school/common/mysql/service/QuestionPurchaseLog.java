package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/17 14:37
 * description:
 */
public interface QuestionPurchaseLog extends Base<com.example.school.common.mysql.entity.QuestionPurchaseLog, Long> {

    void saveQuestionPurchaseLog(Long userId, Long questionBankId);

    Optional<com.example.school.common.mysql.entity.QuestionPurchaseLog> findQuestionPurchaseLog(Long userId, Long questionBankId);


}
