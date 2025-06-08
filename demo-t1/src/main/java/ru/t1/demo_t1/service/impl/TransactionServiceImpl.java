package ru.t1.demo_t1.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.demo_t1.aop.annotation.Cached;
import ru.t1.demo_t1.aop.annotation.Metric;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.util.TransactionMapper;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.repository.TransactionRepository;
import ru.t1.demo_t1.service.TransactionService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private  TransactionRepository transactionRepository;
    @Autowired
    private  TransactionMapper transactionMapper;

    @Override
    @Metric
    @Cached
    public List<TransactionDTO> getTransactions() {
        log.info("Getting transactions");
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Metric
    @Cached(key = "id")
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()->new NoEntityException("Транзакции с id " + id + " не существует"));

        return TransactionDTO.builder()
                .accountId(transaction.getAccount().getId())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    @Override
    @Metric
    @Cached(key = "accountId")
    public TransactionDTO getTransactionByAccountId(Long accountId) {
        Transaction transaction = transactionRepository.findByAccountId(accountId).orElseThrow(()->new NoEntityException("Транзакции с account id " + accountId + " не существует"));

        return TransactionDTO.builder()
                .accountId(transaction.getAccount().getId())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    @Override
    @Cached(key = "transactionId")
    public Transaction getTransactionByTransactionId(UUID transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId).orElseThrow(()->new NoEntityException("Транзакции с transaction id " + transactionId + " не существует"));
        return transaction;
    }

    @Override
    @Metric
    public void createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transactionRepository.save(transaction);
    }

    @Override
    @Metric
    public void deleteTransactionById(Long id) {
        transactionRepository.findById(id).orElseThrow(()->new NoEntityException("Транзакции с id " + id + "  не существует."));
        transactionRepository.deleteById(id);
    }

    @Override
    @Metric
    public void updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()->new NoEntityException("Транзакции с id " + id + "  не существует."));

        if (!transactionDTO.getTransactionId().equals(transaction.getTransactionId())) {
            throw new IllegalArgumentException("Переданный transactionId отличается от существующего");
        }
        transaction.setStatus(transactionDTO.getStatus());

        transactionRepository.save(transaction);
    }
}
