package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Transaction;
import com.example.school.common.mysql.repo.TransactionRepository;
import com.example.school.common.mysql.service.TopicService;
import com.example.school.common.mysql.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.school.common.base.service.SearchFilter.Operator;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 16:10
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TopicService topicService;

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public Transaction save(Transaction transaction) {
        Long id = transaction.getId();
        if (id != null && id != 0L) {
            Optional<Transaction> transactionOptional = findByIdNotDelete(id);
            if (transactionOptional.isPresent()) {
                Transaction transactionDB = transactionOptional.get();
                transactionDB.setTitle(transaction.getTitle());
                transactionDB.setPrice(transaction.getPrice());
                transactionDB.setDescribeContent(transaction.getDescribeContent());
                transactionDB.setContactMode(transaction.getContactMode());
                transactionDB.setContactPeople(transaction.getContactPeople());
                transactionDB.setAddress(transaction.getAddress());
                return transactionRepository.save(transactionDB);
            }
            return null;
        } else {
            transaction.setState(IN_RELEASE);
            transaction.setCreatedTime(currentDateTime());
            transaction.setDeleteState(UN_DELETED);
            return transactionRepository.save(transaction);
        }
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void deleteById(Long aLong) {
        transactionRepository.findById(aLong).ifPresent(transaction -> {
            transaction.setDeleteState(DELETED);
            transactionRepository.save(transaction);
        });
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public Optional<Transaction> findByIdNotDelete(Long aLong) {
        return transactionRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public Page<Transaction> findPageByEntity(Transaction transaction) {
        return null;
    }

    @Override
    public RoTransaction saveTransaction(Transaction transaction) {
        transaction = this.save(transaction);
        return topicService.resultRoTransaction(transaction, transaction.getUserId(), TOPIC_TYPE_1, ZAN_TOPIC);
    }

    @Override
    public void deleteTransaction(Long id) {
        this.deleteById(id);
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void modifyTransactionSateToNewRelease(Long id) {
        transactionRepository.findById(id).ifPresent(transaction -> {
            transaction.setState(NEW_RELEASE);
            transactionRepository.save(transaction);
        });
    }


    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void modifyTransactionSateToAfterRelease(List<Long> userId) {
        transactionRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public RoTransaction findTransaction(Long id, Long userId) throws OperationException {
        Transaction transaction = this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
        return topicService.resultRoTransaction(transaction, userId, TOPIC_TYPE_1, ZAN_TOPIC);
    }


    @Override
    public CustomPage<RoTransaction> findTransactionEffectivePage(Transaction transaction, Long userId) {
        Map<String, SearchFilter> filters = getTransactionFilter(getEffectiveState(), transaction);
        return getRoTransactionCustomPage(transaction, userId, filters);

    }

    @Override
    public CustomPage<RoTransaction> findTransactionUserPage(Transaction transaction, Long userId) {
        Map<String, SearchFilter> filters = getTransactionFilter(getEffectiveState(), transaction);
        filters.put("userId", new SearchFilter("userId", transaction.getUserId(), Operator.EQ));
        return getRoTransactionCustomPage(transaction, userId, filters);

    }

    private CustomPage<RoTransaction> getRoTransactionCustomPage(Transaction transaction, Long userId, Map<String, SearchFilter> filters) {
        Specification<Transaction> specification = SearchFilter.bySearchFilter(filters.values());
        Pageable pageable = PageUtils.buildPageRequest(transaction);
        Page<Transaction> page = transactionRepository.findAll(specification, pageable);
        return topicService.resultRoTransactionPage(page, userId, TOPIC_TYPE_1, ZAN_TOPIC);
    }

    private Map<String, SearchFilter> getTransactionFilter(Map<String, SearchFilter> filters, Transaction transaction) {
        if (transaction.getStartDateTime() != null && transaction.getEndDateTime() != null) {
            filters.put("createdTimeStart", new SearchFilter("createdTime", transaction.getStartDateTime(), Operator.GTE));
            filters.put("createdTimeEnd", new SearchFilter("createdTime", transaction.getEndDateTime(), Operator.LTE));
        }
        return filters;
    }
}
