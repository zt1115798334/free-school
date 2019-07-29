package com.example.school.common.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoQuestionBank;
import com.example.school.common.base.service.BaseService;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.QuestionBank;
import org.springframework.data.domain.PageImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/17 14:37
 * description:
 */
public interface QuestionBankService extends BaseService<QuestionBank, Long> {

    RoQuestionBank saveQuestionBank(QuestionBank questionBank);

    void saveQuestionBankFile(HttpServletRequest request, Long topicId);

    void deleteQuestionBank(Long id);

    void modifyQuestionBankSateToNewRelease(Long id);

    void modifyQuestionBankSateToAfterRelease(List<Long> userId);

    void incrementQuestionBankBrowsingVolume(Long id);

    QuestionBank findQuestionBank(Long id) ;

    RoQuestionBank findRoQuestionBank(Long id, Long userId) ;

    PageImpl<RoQuestionBank> findQuestionBankEffectivePage(QuestionBank questionBank, Long userId);

    PageImpl<RoQuestionBank> findQuestionBankUserPage(QuestionBank questionBank, Long userId);

    PageImpl<RoQuestionBank> findQuestionBankCollectionPage(CustomPage customPage, Long userId);

    JSONObject findQuestionBankNonExistentAnswer(Long id) ;

    JSONObject findQuestionBankExistenceAnswer(Long id, Long userId) ;

}
