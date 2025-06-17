package ru.t1.demo_t1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.t1.core.model.BlacklistRequest;
import ru.t1.core.model.BlacklistResponse;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.demo_t1.config.TransactionConfig;
import ru.t1.demo_t1.kafka.TransactionProducer;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.dto.JwtResponse;
import ru.t1.demo_t1.model.dto.LoginRequest;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.model.event.TransactionEvent;
import ru.t1.demo_t1.service.AccountService;
import ru.t1.demo_t1.service.ClientService;
import ru.t1.demo_t1.service.TransactionConsumerService;
import ru.t1.demo_t1.service.TransactionService;
import ru.t1.demo_t1.mapper.AccountRequestMapper;
import ru.t1.demo_t1.mapper.TransactionMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumerServiceImpl implements TransactionConsumerService {
    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final ClientService clientService;
    @Autowired
    private final TransactionProducer transactionProducer;
    @Autowired
    private final TransactionMapper transactionMapper;
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final AccountRequestMapper accountRequestMapper;
    @Autowired
    private final TransactionConfig transactionConfig;
    private static String jwtToken = null;

    @Override
    public void processClientStatus(Client client, Account account, TransactionEvent transactionEvent) {

        StatusClientEnum statusClient = client.getStatus();

        if (statusClient == null) {
            RestTemplate restTemplate = new RestTemplate();
            if (jwtToken == null) {
                getJwtToken(restTemplate);
            }

            ResponseEntity<BlacklistResponse> response = generateResponse(client, account, restTemplate);

            if (response.getStatusCode() == HttpStatus.OK) {
                statusClient = response.getBody().getClientStatus();
            } else {
                log.error("Http-request failed with http status {}", response.getStatusCode());
                throw new RuntimeException("Http request failed");
            }
        } else {
            List<TransactionDTO> transactions = transactionService.getTransactionByAccountIdAndStatus(UUID
                    .fromString(transactionEvent.getAccountId()), TransactionStatusEnum.REJECTED);

            if (transactions.size() > transactionConfig.getMaxRejectedTransaction()) {
                account.setStatus(AccountStatusEnum.ARRESTED);
            }
        }

        if (statusClient.equals(StatusClientEnum.BLOCKED)) {
            client.setStatus(StatusClientEnum.BLOCKED);
            account.setStatus(AccountStatusEnum.BLOCKED);
            clientService.update(client);
        }
    }

    private ResponseEntity<BlacklistResponse> generateResponse(Client client, Account account, RestTemplate restTemplate) {
        String url = "http://localhost:8081/blacklist/verification";
        BlacklistRequest blacklistRequest = BlacklistRequest.builder()
                .clientId(client.getClientId())
                .accountId(account.getAccountId())
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpEntity = new HttpEntity<>(blacklistRequest, httpHeaders);

        return restTemplate.exchange(url,
                HttpMethod.POST,
                httpEntity,
                BlacklistResponse.class
        );
    }

    private void getJwtToken(RestTemplate restTemplate) {
        try {
            jwtToken = restTemplate.postForEntity("http://localhost:8081/api/auth/signin",
                    new LoginRequest("user", "password"),
                    JwtResponse.class).getBody().getAccessToken();
        } catch (NullPointerException e) {
            log.error("The jwt token was not received. Exception message: {}", e.getMessage());
            throw new IllegalArgumentException("JwtToken is null");
        }
        log.info("Generated JwtToken has been received: {}", jwtToken);
    }

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
}
