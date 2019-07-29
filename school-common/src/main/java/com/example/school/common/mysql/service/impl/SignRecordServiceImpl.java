package com.example.school.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.SignRecord;
import com.example.school.common.mysql.repo.SignRecordRepository;
import com.example.school.common.mysql.service.SignRecordService;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.NumberUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class SignRecordServiceImpl implements SignRecordService {


    private final SignRecordRepository signRecordRepository;

    @Override
    public void signIn(Long userId) {
        LocalDate currentDate = currentDate();
        LocalDate firstDay = DateUtils.dateToMonthFirstDay(currentDate);
        int currentDayOfMonth = currentDate.getDayOfMonth();
        int dayOfMonthDis = 1 << currentDayOfMonth;
        Optional<SignRecord> signRecordOptional = signRecordRepository.findByUserIdAndDateMonth(userId, firstDay);
        if (signRecordOptional.isPresent()) {
            SignRecord signRecord = signRecordOptional.get();
            int mask = new BigInteger(signRecord.getMask(), 2).intValue();
            if ((mask & dayOfMonthDis) == 0) {
                signRecord.setMask(NumberUtils.intToBinary(dayOfMonthDis | mask));
                Integer continueSignMonth = signRecord.getContinueSignMonth();
                if ((mask & (1 << (currentDayOfMonth - 1))) == 0) {
                    continueSignMonth = 1;
                } else {
                    continueSignMonth++;
                }

                signRecord.setContinueSignMonth(continueSignMonth);
                signRecordRepository.save(signRecord);
            } else {
                throw new OperationException("你已经签到了");
            }
        } else {
            String dayOfMonthBinary = NumberUtils.intToBinary(dayOfMonthDis);
            SignRecord signRecord = new SignRecord(userId, firstDay, dayOfMonthBinary, 1);
            signRecordRepository.save(signRecord);
        }

    }

    @Override
    public void complementSigned(Long userId, Integer dayOfMonth) {

    }

    @Override
    public List<JSONObject> signInCalendar(Long userId, LocalDate dateMonth) {
        Optional<SignRecord> signRecordOptional = signRecordRepository.findByUserIdAndDateMonth(userId, dateMonth);
        return signRecordOptional.map(signRecord -> {
            List<LocalDate> localDates = DateUtils.dateRangeList(DateUtils.dateToMonthFirstDay(dateMonth),
                    DateUtils.dateToMonthLastDay(dateMonth));
            String reverseMake = new StringBuilder(signRecord.getMask()).reverse().toString();
            return localDates.stream().map(localDate -> {
                JSONObject result = new JSONObject();
                result.put("date", DateUtils.formatDate(localDate));
                result.put("signState", reverseMake.charAt(localDate.getDayOfMonth()));
                return result;
            }).collect(toList());
        }).orElseGet(Collections::emptyList);
    }


}
