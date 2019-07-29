package com.example.school.common.mysql.service.impl;

import com.example.school.common.base.entity.CustomPage;
import com.example.school.common.base.entity.ro.RoTransaction;
import com.example.school.common.base.service.PageUtils;
import com.example.school.common.base.service.SearchFilter;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.Transaction;
import com.example.school.common.mysql.repo.TransactionRepository;
import com.example.school.common.mysql.service.CollectionService;
import com.example.school.common.mysql.service.TopicService;
import com.example.school.common.mysql.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TopicService topicService;

    private final CollectionService collectionService;

    @Override
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
            transaction.setBrowsingVolume(0L);
            transaction.setState(IN_RELEASE);
            transaction.setCreatedTime(currentDateTime());
            transaction.setDeleteState(UN_DELETED);
            return transactionRepository.save(transaction);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        transactionRepository.findById(aLong).ifPresent(transaction -> {
            transaction.setDeleteState(DELETED);
            transactionRepository.save(transaction);
        });
    }

    @Override
    public Optional<Transaction> findByIdNotDelete(Long aLong) {
        return transactionRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<Transaction> findByIdsNotDelete(List<Long> id) {
        return transactionRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<Transaction> findPageByEntity(Transaction transaction) {
        return null;
    }

    @Override
    public RoTransaction saveTransaction(Transaction transaction) {
        transaction = this.save(transaction);
        return topicService.resultRoTransaction(transaction, transaction.getUserId());
    }

    @Override
    public void deleteTransaction(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyTransactionSateToNewRelease(Long id) {
        transactionRepository.findById(id).ifPresent(transaction -> {
            transaction.setState(NEW_RELEASE);
            transactionRepository.save(transaction);
        });
    }


    @Override
    public void modifyTransactionSateToAfterRelease(List<Long> userId) {
        transactionRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public void incrementTransactionBrowsingVolume(Long id) {
        transactionRepository.incrementBrowsingVolume(id);
    }

    @Override
    public Transaction findTransaction(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoTransaction findRoTransaction(Long id, Long userId) {
        Transaction transaction = this.findTransaction(id);
        this.incrementTransactionBrowsingVolume(id);
        return topicService.resultRoTransaction(transaction, userId);
    }


    @Override
    public PageImpl<RoTransaction> findTransactionEffectivePage(Transaction transaction, Long userId) {
        List<SearchFilter> filters = getTransactionFilter(getEffectiveState(), transaction);
        return getRoTransactionCustomPage(transaction, userId, filters);

    }

    @Override
    public PageImpl<RoTransaction> findTransactionUserPage(Transaction transaction, Long userId) {
        List<SearchFilter> filters = getTransactionFilter(getEffectiveState(), transaction);
        filters.add(new SearchFilter("userId", transaction.getUserId(), Operator.EQ));
        return getRoTransactionCustomPage(transaction, userId, filters);

    }

    @Override
    public PageImpl<RoTransaction> findTransactionCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, TOPIC_TYPE_1, customPage);
        List<Transaction> transactionList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoTransactionPage(new PageImpl<>(transactionList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoTransaction> getRoTransactionCustomPage(Transaction transaction, Long userId, List<SearchFilter> filters) {
        Specification<Transaction> specification = SearchFilter.bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(transaction);
        Page<Transaction> page = transactionRepository.findAll(specification, pageable);
        return topicService.resultRoTransactionPage(page, userId);
    }

    private List<SearchFilter> getTransactionFilter(List<SearchFilter> filters, Transaction transaction) {
        if (transaction.getStartDateTime() != null && transaction.getEndDateTime() != null) {
            filters.add(new SearchFilter("createdTime", transaction.getStartDateTime(), Operator.GTE));
            filters.add(new SearchFilter("createdTime", transaction.getEndDateTime(), Operator.LTE));
        }
        return filters;
    }
}
