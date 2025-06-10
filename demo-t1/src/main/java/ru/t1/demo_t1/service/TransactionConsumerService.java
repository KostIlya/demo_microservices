package ru.t1.demo_t1.service;

import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.event.TransactionEvent;

import java.util.UUID;

public interface TransactionConsumerService {
    void processClientStatus(Client client, Account account, TransactionEvent transactionEvent);
    void sendMessageToTransactionAccept(Account account, UUID transactionId, TransactionEvent transactionEvent);
}
