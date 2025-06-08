package ru.t1.demo_t1.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.repository.AccountRepository;

@Component
public class TransactionMapper {
    @Autowired
    AccountRepository accountRepository;

    public Transaction toEntity(TransactionDTO transactionDTO) {
        Account account = accountRepository.findById(transactionDTO.getAccountId()).orElseThrow(() -> new NullPointerException("Account с id {} не существует." + transactionDTO.getAccountId()));
        return Transaction.builder()
                .account(account)
                .amount(transactionDTO.getAmount())
                .timestamp(transactionDTO.getTimestamp())
                .transactionId(transactionDTO.getTransactionId())
                .status(transactionDTO.getStatus())
                .build();
    }

    public TransactionDTO toDto(Transaction transaction) {
        return TransactionDTO.builder()
                .accountId(transaction.getAccount().getId())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .transactionId(transaction.getTransactionId())
                .status(transaction.getStatus())
                .build();
    }
}
