package ru.t1.service2.service;


import ru.t1.core.model.event.TransactionAcceptEvent;

public interface TransactionAcceptService {
    void getTransactionResult(TransactionAcceptEvent transactionAcceptEvent);
}
