package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.ro.*;
import com.example.school.common.base.service.ConstantService;
import com.example.school.common.mysql.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface TopicService extends ConstantService {

    RoTransaction resultRoTransaction(Transaction transaction,
                                      Long userId);

    PageImpl<RoTransaction> resultRoTransactionPage(Page<Transaction> page, Long userId);

    RoRecordTime resultRoRecordTime(RecordTime recordTime, Long userId);

    PageImpl<RoRecordTime> resultRoRecordTimePage(Page<RecordTime> page, Long userId);

    RoKnowing resultRoKnowing(Knowing knowing, Long userId);

    PageImpl<RoKnowing> resultRoKnowingPage(Page<Knowing> page, Long userId);

    RoInformation resultRoInformation(Information information, Long userId);

    PageImpl<RoInformation> resultRoInformationPage(Page<Information> page, Long userId);

    RoQuestionBank resultRoQuestionBank(QuestionBank questionBank, Long userId);

    PageImpl<RoQuestionBank> resultRoQuestionBankPage(Page<QuestionBank> page, Long userId);

}
