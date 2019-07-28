package com.example.school.common.mysql.service;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoRecordTime;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.mysql.entity.RecordTime;
import com.example.school.common.mysql.entity.Transaction;
import org.springframework.data.domain.Page;

public interface TopicService {

    RoTransaction resultRoTransaction(Transaction transaction,
                                      Long userId, Short topicTyp, Short zanType);

    CustomPage<RoTransaction> resultRoTransactionPage(Page<Transaction> page,
                                                      Long userId, Short topicTyp, Short zanType);

    RoRecordTime resultRoRecordTime(RecordTime recordTime,
                                    Long userId, Short topicTyp, Short zanType);

    CustomPage<RoRecordTime> resultRoRecordTimePage(Page<RecordTime> page,
                                                    Long userId, Short topicTyp, Short zanType);
}
