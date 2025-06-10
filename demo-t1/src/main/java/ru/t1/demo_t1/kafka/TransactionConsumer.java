package ru.t1.demo_t1.kafka;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.demo_t1.model.event.TransactionEvent;
import ru.t1.core.model.event.TransactionResultEvent;
import ru.t1.demo_t1.repository.ClientRepository;
import ru.t1.demo_t1.service.AccountService;
import ru.t1.demo_t1.service.ClientService;
import ru.t1.demo_t1.service.TransactionConsumerService;
import ru.t1.demo_t1.service.TransactionService;
import ru.t1.demo_t1.util.AccountRequestMapper;
import ru.t1.demo_t1.util.TransactionMapper;

import java.util.List;
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
    private ClientService clientService;
    @Autowired
    private TransactionConsumerService transactionConsumerService;

    @KafkaListener(topics = "t1_demo_transactions")
    @Transactional
    public void handleTransactionInput(TransactionEvent transactionEvent) {
        UUID transactionId = UUID.randomUUID();
        log.info("t1_demo_transactions: received transaction: {}", transactionId);

        Client client;
        try {
            client = clientService.getClientByClientId(UUID.fromString(transactionEvent.getClientId()));
        } catch (NoEntityException e) {
            log.error("handleTransactionInput: {}", e.getMessage());
            return;
        }
        Account account;
        try {
            account = accountService.getAccountByAccountId(UUID.fromString(transactionEvent.getAccountId()));
        } catch (NoEntityException e) {
            log.error("handleTransactionInput: {}", e.getMessage());
            return;
        }

        transactionConsumerService.processClientStatus(client, account, transactionEvent);

        transactionConsumerService.sendMessageToTransactionAccept(account, transactionId, transactionEvent);

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
