package ru.t1.demo_t1.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.demo_t1.config.TransactionConfig;
import ru.t1.demo_t1.kafka.TransactionProducer;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.model.event.TransactionEvent;
import ru.t1.demo_t1.service.AccountService;
import ru.t1.demo_t1.service.ClientService;
import ru.t1.demo_t1.service.TransactionConsumerService;
import ru.t1.demo_t1.service.TransactionService;
import ru.t1.demo_t1.util.AccountRequestMapper;
import ru.t1.demo_t1.util.TransactionMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TransactionConsumerServiceImpl implements TransactionConsumerService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionProducer transactionProducer;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRequestMapper accountRequestMapper;
    @Autowired
    private TransactionConfig transactionConfig;

    @Override
    public void sendMessageToTransactionAccept(Account account, UUID transactionId, TransactionEvent transactionEvent) {
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
            transactionDTO.setStatus(TransactionStatusEnum.REJECTED);
        }

        transactionService.createTransaction(transactionDTO);
    }

    @Override
    public void processClientStatus(Client client, Account account, TransactionEvent transactionEvent) {

        //TODO: отправка запроса в сервис для верификации клиента
        StatusClientEnum statusClient = StatusClientEnum.ACTIVE;


        if (client.getStatus() == null) {
            //todo: here send http request to service2 /blacklist/verification
        } else {
            //TODO: Если статус клиента при получении сообщения о транзакции известен
            // и транзакции в статусе REJECTED уже существуют, то если приходит более N
            // (значение настраивается в конфиге) транзакций, по-прежнему выставить их в
            // REJECTED, счету присвоить статус ARRESTED.
            List<TransactionDTO> transactions = transactionService.getTransactionByAccountId(UUID
                    .fromString(transactionEvent.getAccountId()));
            int countRejectedTransactions = 0;
            for (var t : transactions) {
                if (t.getStatus().equals(TransactionStatusEnum.REJECTED)) {
                    countRejectedTransactions++;
                }
            }
            if (countRejectedTransactions > transactionConfig.getMaxRejectedTransaction()) {
                account.setStatus(AccountStatusEnum.ARRESTED);
            }
        }

        if (statusClient.equals(StatusClientEnum.BLOCKED)) {
            client.setStatus(StatusClientEnum.BLOCKED);
            account.setStatus(AccountStatusEnum.BLOCKED);
            clientService.update(client);
        }

    }
}
