package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.QuestionPurchaseLogRepository;
import com.example.school.common.mysql.service.QuestionPurchaseLog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/17 14:37
 * description:
 */
@AllArgsConstructor
@Service
public class QuestionPurchaseLogImpl implements QuestionPurchaseLog {


    private final QuestionPurchaseLogRepository questionPurchaseLogRepository;

    @Override
    public com.example.school.common.mysql.entity.QuestionPurchaseLog save(com.example.school.common.mysql.entity.QuestionPurchaseLog questionPurchaseLog) {
        questionPurchaseLog.setCreatedTime(currentDateTime());
        return questionPurchaseLogRepository.save(questionPurchaseLog);
    }

    @Override
    public void saveQuestionPurchaseLog(Long userId, Long questionBankId) {
        Optional<com.example.school.common.mysql.entity.QuestionPurchaseLog> logOptional = this.findQuestionPurchaseLog(userId, questionBankId);
        if (!logOptional.isPresent()) {
            this.save(new com.example.school.common.mysql.entity.QuestionPurchaseLog(userId, questionBankId));
        }
    }

    @Override
    public Optional<com.example.school.common.mysql.entity.QuestionPurchaseLog> findQuestionPurchaseLog(Long userId, Long questionBankId) {
        return questionPurchaseLogRepository.findByUserIdAndQuestionBankId(userId, questionBankId);
    }
}
