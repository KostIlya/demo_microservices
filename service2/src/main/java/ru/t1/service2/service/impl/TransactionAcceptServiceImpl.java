package ru.t1.service2.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.service2.config.LimitConfig;
import ru.t1.service2.kafka.TransactionProducer;
import ru.t1.service2.model.*;
import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.core.model.event.TransactionAcceptEvent;
import ru.t1.core.model.event.TransactionResultEvent;
import ru.t1.service2.repository.TransactionAcceptRepository;
import ru.t1.service2.service.TransactionAcceptService;
import ru.t1.service2.util.TransactionAcceptMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionAcceptServiceImpl implements TransactionAcceptService {
    @Autowired
    private TransactionAcceptMapper transactionAcceptMapper;
    @Autowired
    private TransactionAcceptRepository transactionAcceptRepository;
    @Autowired
    private LimitConfig limitConfig;
    @Autowired
    private TransactionProducer transactionProducer;

    @Override
    public void getTransactionResult(TransactionAcceptEvent transactionAcceptEvent) {
        TransactionStatusEnum status;

        TransactionAccept transactionAccept = transactionAcceptMapper.toEntity(transactionAcceptEvent);
        if (isLimitPerPeriod(transactionAccept)) {
            status = TransactionStatusEnum.BLOCKED;
        } else if (transactionAcceptEvent.getAmount().compareTo(transactionAcceptEvent.getBalance()) > 0) {
            status = TransactionStatusEnum.REJECTED;
        } else {
            status = TransactionStatusEnum.ACCEPTED;
        }

        transactionAcceptRepository.save(transactionAccept);

        TransactionResultEvent transactionResultEvent = TransactionResultEvent.builder()
                .transactionId(transactionAcceptEvent.getTransactionId())
                .accountId(transactionAcceptEvent.getAccountId())
                .status(status)
                .build();
        transactionProducer.sendResult(transactionResultEvent);
        log.info("getTransactionResult(): the transaction has received the status {}", status);
    }

    private boolean isLimitPerPeriod(TransactionAccept transactionAccept) {
        LocalDateTime start = transactionAccept.getTimestamp().minusNanos(limitConfig.limitTime);
        List<TransactionAccept> transactionAcceptList = transactionAcceptRepository
                .findByClientIdAndAccountIdAndPeriod(transactionAccept.getClientId(), transactionAccept.getAccountId(),
                        start, transactionAccept.getTimestamp());

        if (transactionAcceptList.size() == limitConfig.maxNumberTransactionsPerLimitTime) {
            log.info("isLimitPerPeriod(): the limit on the number of transactions in a limited time has been exceeded");
            for (var t : transactionAcceptList) {
                TransactionResultEvent transactionResultEvent = TransactionResultEvent.builder()
                        .transactionId(t.getTransactionId())
                        .status(TransactionStatusEnum.BLOCKED)
                        .accountId(t.getAccountId())
                        .build();
                transactionProducer.sendResult(transactionResultEvent);
            }
            return true;
        }

        return false;
    }
}
