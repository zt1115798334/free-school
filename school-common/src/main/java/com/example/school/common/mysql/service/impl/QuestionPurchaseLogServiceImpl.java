package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.entity.QuestionPurchaseLog;
import com.example.school.common.mysql.repo.QuestionPurchaseLogRepository;
import com.example.school.common.mysql.service.QuestionPurchaseLogService;
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
public class QuestionPurchaseLogServiceImpl implements QuestionPurchaseLogService {


    private final QuestionPurchaseLogRepository questionPurchaseLogRepository;

    @Override
    public QuestionPurchaseLog save(QuestionPurchaseLog questionPurchaseLog) {
        questionPurchaseLog.setCreatedTime(currentDateTime());
        return questionPurchaseLogRepository.save(questionPurchaseLog);
    }

    @Override
    public void saveQuestionPurchaseLog(Long userId, Long questionBankId) {
        Optional<QuestionPurchaseLog> logOptional = this.findQuestionPurchaseLog(userId, questionBankId);
        if (!logOptional.isPresent()) {
            this.save(new QuestionPurchaseLog(userId, questionBankId));
        }
    }

    @Override
    public Optional<QuestionPurchaseLog> findQuestionPurchaseLog(Long userId, Long questionBankId) {
        return questionPurchaseLogRepository.findByUserIdAndQuestionBankId(userId, questionBankId);
    }
}
