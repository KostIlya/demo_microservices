package ru.t1.demo_t1.kafka;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.demo_t1.model.event.TransactionEvent;
import ru.t1.core.model.event.TransactionResultEvent;
import ru.t1.demo_t1.service.AccountService;
import ru.t1.demo_t1.service.TransactionService;
import ru.t1.demo_t1.util.AccountRequestMapper;
import ru.t1.demo_t1.util.TransactionMapper;

import java.util.UUID;

@Component
@Slf4j
public class TransactionConsumer {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRequestMapper accountRequestMapper;
    @Autowired
    private TransactionProducer transactionProducer;

    @KafkaListener(topics = "t1_demo_transactions")
    @Transactional
    public void handleTransactionInput(TransactionEvent transactionEvent) {
        UUID transactionId = UUID.randomUUID();
        log.info("t1_demo_transactions: received transaction: {}", transactionId);
        Account account;
        try {
            account = accountService.getAccountByAccountId(UUID.fromString(transactionEvent.getAccountId()));
        } catch (NoEntityException e) {
            log.error("handleTransactionInput: {}", e.getMessage());
            return;
        }
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountId(account.getId())
                .transactionId(transactionId)
                .amount(transactionEvent.getAmount())
                .timestamp(transactionEvent.getTimestamp())
                .build();

        if (account.getStatus() == AccountStatusEnum.OPEN) {
            transactionDTO.setStatus(TransactionStatusEnum.REQUESTED);

            String messageId = transactionProducer.sendAccept(transactionMapper.toEntity(transactionDTO));
            account.setBalance(account.getBalance().subtract(transactionEvent.getAmount()));
            accountService.updateAccount(account.getId(), accountRequestMapper.toDto(account));
            log.info("Message sent topic t1_demo_transaction_accept with id: {}", messageId);
        } else {
            log.error("Transaction cancelled, because account status: {}", account.getStatus());
            transactionDTO.setStatus(TransactionStatusEnum.CANCELLED);
        }

        transactionService.createTransaction(transactionDTO);
        log.info("t1_demo_transactions: transaction saved in database: {}", transactionId);
    }

    @KafkaListener(topics = "t1_demo_transaction_result")
    @Transactional
    public void handleTransactionResult(TransactionResultEvent transactionResultEvent) {
        log.info("t1_demo_transaction_result: received transaction: {}", transactionResultEvent.getTransactionId());
        Transaction transaction = transactionService.getTransactionByTransactionId(transactionResultEvent.getTransactionId());
        if (transactionResultEvent.getStatus().equals(TransactionStatusEnum.REJECTED) || transactionResultEvent.getStatus().equals(TransactionStatusEnum.BLOCKED)) {
            Account account = accountService.getAccountByAccountId(transactionResultEvent.getAccountId());

            account.setBalance(account.getBalance().add(transaction.getAmount()));
            if (transactionResultEvent.getStatus().equals(TransactionStatusEnum.BLOCKED)) {
                account.setStatus(AccountStatusEnum.BLOCKED);
                account.setFrozenAmount(transaction.getAmount());
            }
            accountService.updateAccount(account.getId(), accountRequestMapper.toDto(account));
        }
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        transactionDTO.setStatus(transactionResultEvent.getStatus());
        transactionService.updateTransaction(transaction.getId(), transactionDTO);
        log.info("t1_demo_transaction_result: transaction saved in database: {}", transactionResultEvent.getTransactionId());
    }


}
