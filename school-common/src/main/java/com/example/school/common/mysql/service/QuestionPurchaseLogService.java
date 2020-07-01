package com.example.school.common.mysql.service;

import com.example.school.common.base.service.Base;
import com.example.school.common.mysql.entity.QuestionPurchaseLog;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/17 14:37
 * description:
 */
public interface QuestionPurchaseLogService extends Base<QuestionPurchaseLog, Long> {

    void saveQuestionPurchaseLog(Long userId, Long questionBankId);

    Optional<QuestionPurchaseLog> findQuestionPurchaseLog(Long userId, Long questionBankId);


}
