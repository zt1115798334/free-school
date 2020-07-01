package com.example.school.task.task.handler.impl;

import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.service.*;
import com.example.school.task.task.handler.UserPageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/24 15:25
 * description:
 */
@AllArgsConstructor
@Slf4j
@Component("modifySateToAfterReleaseHandler")
public class ModifySateToAfterReleaseHandler extends UserPageHandler {

    private final Transaction transactionService;

    private final RecordTime recordTimeService;

    private final Knowing knowingService;

    private final Information informationService;

    private final QuestionBank questionBankService;

    @Override
    protected int handleDataOfPerPage(List<User> list, int pageNumber) {
        List<Long> userIdList = list.parallelStream().map(User::getId).collect(Collectors.toList());
        transactionService.modifyTransactionSateToAfterRelease(userIdList);
        recordTimeService.modifyRecordTimeSateToAfterRelease(userIdList);
        knowingService.modifyKnowingSateToAfterRelease(userIdList);
        informationService.modifyInformationSateToAfterRelease(userIdList);
        questionBankService.modifyQuestionBankSateToAfterRelease(userIdList);
        return list.size();
    }
}
