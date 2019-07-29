package com.example.school.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoQuestionBank;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.FileInfo;
import com.example.school.common.mysql.entity.QuestionBank;
import com.example.school.common.mysql.entity.QuestionPurchaseLog;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.repo.QuestionBankRepository;
import com.example.school.common.mysql.service.*;
import com.example.school.common.utils.FileUtils;
import com.example.school.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.Operator;
import static com.example.school.common.base.service.SearchFilter.bySearchFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/17 14:37
 * description:
 */
@AllArgsConstructor
@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    private final QuestionBankRepository questionBankRepository;

    private final QuestionPurchaseLogService questionPurchaseLogService;

    private final UserService userService;

    private final TopicService topicService;

    private final CollectionService collectionService;

    private final FileInfoService fileInfoService;

    @Override
    public QuestionBank save(QuestionBank questionBank) {
        Long id = questionBank.getId();
        if (id != null && id != 0L) {
            Optional<QuestionBank> questionBankOptional = findByIdNotDelete(id);
            if (questionBankOptional.isPresent()) {
                QuestionBank questionBankDB = questionBankOptional.get();
                questionBankDB.setTitle(questionBank.getTitle());
                questionBankDB.setDescribeContent(questionBank.getDescribeContent());
                return questionBankRepository.save(questionBankDB);
            }
            return null;
        } else {
            questionBank.setBrowsingVolume(0L);
            questionBank.setState(IN_RELEASE);
            questionBank.setCreatedTime(currentDateTime());
            questionBank.setDeleteState(UN_DELETED);
            return questionBankRepository.save(questionBank);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        questionBankRepository.findById(aLong).ifPresent(questionBank -> {
            questionBank.setDeleteState(DELETED);
            questionBankRepository.save(questionBank);
        });
    }

    @Override
    public Optional<QuestionBank> findByIdNotDelete(Long aLong) {
        return questionBankRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<QuestionBank> findByIdsNotDelete(List<Long> id) {
        return questionBankRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<QuestionBank> findPageByEntity(QuestionBank questionBank) {
        return null;
    }

    @Override
    public RoQuestionBank saveQuestionBank(QuestionBank questionBank) {
        questionBank = this.save(questionBank);
        return topicService.resultRoQuestionBank(questionBank, questionBank.getUserId());

    }

    @Override
    public void saveQuestionBankFile(HttpServletRequest request, Long topicId) {
        String folderPath = FileUtils.getFolderPath(FOLDER_QUESTION_BANK_FILE);
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        UploadFile nonExistentAnswerFile = FileUtils.uploadFile(multiRequest.getFile("nonExistentAnswer"), folderPath);
        UploadFile existenceAnswerFile = FileUtils.uploadFile(multiRequest.getFile("existenceAnswer"), folderPath);

        FileInfo nonExistentAnswerFileInfo = fileInfoService.saveFileInfo(nonExistentAnswerFile);
        FileInfo existenceAnswerFileInfo = fileInfoService.saveFileInfo(existenceAnswerFile);

        questionBankRepository.findById(topicId).ifPresent(questionBank -> {
            questionBank.setNonExistentAnswer(nonExistentAnswerFileInfo.getId());
            questionBank.setExistenceAnswer(existenceAnswerFileInfo.getId());
            questionBankRepository.save(questionBank);
        });
    }

    @Override
    public void deleteQuestionBank(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyQuestionBankSateToNewRelease(Long id) {
        questionBankRepository.findById(id).ifPresent(questionBank -> {
            questionBank.setState(NEW_RELEASE);
            questionBankRepository.save(questionBank);
        });
    }

    @Override
    public void modifyQuestionBankSateToAfterRelease(List<Long> userId) {
        questionBankRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public void incrementQuestionBankBrowsingVolume(Long id) {
        questionBankRepository.incrementBrowsingVolume(id);
    }

    @Override
    public QuestionBank findQuestionBank(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoQuestionBank findRoQuestionBank(Long id, Long userId) {
        QuestionBank questionBank = this.findQuestionBank(id);
        this.incrementQuestionBankBrowsingVolume(id);
        return topicService.resultRoQuestionBank(questionBank, userId);
    }


    @Override
    public PageImpl<RoQuestionBank> findQuestionBankEffectivePage(QuestionBank questionBank, Long userId) {
        List<SearchFilter> filters = getQuestionBankFilter(getEffectiveState(), questionBank);
        return getRoQuestionBankCustomPage(questionBank, userId, filters);

    }

    @Override
    public PageImpl<RoQuestionBank> findQuestionBankUserPage(QuestionBank questionBank, Long userId) {
        List<SearchFilter> filters = getQuestionBankFilter(getEffectiveState(), questionBank);
        filters.add(new SearchFilter("userId", questionBank.getUserId(), Operator.EQ));
        return getRoQuestionBankCustomPage(questionBank, userId, filters);

    }

    @Override
    public PageImpl<RoQuestionBank> findQuestionBankCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, TOPIC_TYPE_4, customPage);
        List<QuestionBank> questionBankList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoQuestionBankPage(new PageImpl<>(questionBankList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    @Override
    public JSONObject findQuestionBankNonExistentAnswer(Long id) {
        QuestionBank questionBank = this.findQuestionBank(id);
        JSONObject result = new JSONObject();
        result.put("url", SysConst.QUESTION_BANK_PDF_FILE_URL + questionBank.getNonExistentAnswer());
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public JSONObject findQuestionBankExistenceAnswer(Long id, Long userId) {
        Optional<QuestionPurchaseLog> logOptional = questionPurchaseLogService.findQuestionPurchaseLog(id, userId);
        QuestionBank questionBank = this.findQuestionBank(id);
        long existenceAnswer;
        if (logOptional.isPresent()) {
            existenceAnswer = questionBank.getExistenceAnswer();
        } else {
            User user = userService.findByUserId(userId);
            Long integral = questionBank.getIntegral();
            if (user.getIntegral() < integral) {
                throw new OperationException("积分不够，请充值");
            }
            userService.reduceIntegral(userId, integral);
            questionPurchaseLogService.saveQuestionPurchaseLog(id, userId);
            existenceAnswer = questionBank.getExistenceAnswer();
        }
        JSONObject result = new JSONObject();
        result.put("url", SysConst.QUESTION_BANK_PDF_FILE_URL + existenceAnswer);
        return result;
    }

    private PageImpl<RoQuestionBank> getRoQuestionBankCustomPage(QuestionBank questionBank, Long userId, List<SearchFilter> filters) {
        Specification<QuestionBank> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(questionBank);
        Page<QuestionBank> page = questionBankRepository.findAll(specification, pageable);
        return topicService.resultRoQuestionBankPage(page, userId);
    }

    private List<SearchFilter> getQuestionBankFilter(List<SearchFilter> filters, QuestionBank questionBank) {
        if (questionBank.getStartDateTime() != null && questionBank.getEndDateTime() != null) {
            filters.add(new SearchFilter("createdTime", questionBank.getStartDateTime(), Operator.GTE));
            filters.add(new SearchFilter("createdTime", questionBank.getEndDateTime(), Operator.LTE));
        }
        return filters;
    }


}
