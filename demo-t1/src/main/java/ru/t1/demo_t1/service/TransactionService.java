package ru.t1.demo_t1.service;

import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.demo_t1.model.dto.TransactionDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDTO> getTransactions();
    TransactionDTO getTransactionById(Long id);
    List<TransactionDTO> getTransactionByAccountId(UUID account_id);
    List<TransactionDTO> getTransactionByAccountIdAndStatus(UUID accountId, TransactionStatusEnum status);
    Transaction getTransactionByTransactionId(UUID transactionId);
    void createTransaction(TransactionDTO transactionDTO);
    void deleteTransactionById(Long id);
    void updateTransaction(Long id, TransactionDTO transaction);
}
