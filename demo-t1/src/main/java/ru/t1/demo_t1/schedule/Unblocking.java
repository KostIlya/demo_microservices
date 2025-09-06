package ru.t1.demo_t1.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.t1.core.model.RequestUnblock;
import ru.t1.core.model.ResponseUnblock;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.core.model.enums.UnblockStatusEnum;
import ru.t1.demo_aspect_starter.aop.annotation.CountArrestedAccount;
import ru.t1.demo_aspect_starter.aop.annotation.CountBlockedClient;
import ru.t1.demo_t1.config.ScheduleConfig;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.service.AccountService;
import ru.t1.demo_t1.service.ClientService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Unblocking {
    private final ScheduleConfig scheduleConfig;
    private final ClientService clientService;
    private final AccountService accountService;

    @Scheduled(fixedRateString = "${custom.schedule.time}")
    public void unblockClient() {
        String url = "http://localhost:8083/api/unblock/client";
        RestTemplate restTemplate = new RestTemplate();
        ResponseUnblock response;
        List<Client> clients = clientService.getClientsByStatusAndLimit(StatusClientEnum.BLOCKED, scheduleConfig.getAmountClient());
        log.info("unblockClient(): amount blocked clients is {}", clients.size());
        if (clients.isEmpty()) {
            log.info("unblockClient(): stop method, because clients.size() = 0");
            return;
        }
        RequestUnblock requestUnblock = RequestUnblock.builder()
                .ids(clients.stream().map(c -> c.getClientId().toString()).toList())
                .build();

        try {
            response = restTemplate.postForEntity(url, requestUnblock, ResponseUnblock.class).getBody();
        } catch (RestClientException e) {
            log.error("unblockClient(): RestTemplate fail: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        for (var id : response.getMap().keySet()) {
            if (response.getMap().get(id) == UnblockStatusEnum.UNBLOCKED) {
                clientService.clientUpdateStatus(id);
            }
        }
    }



    @Scheduled(fixedRateString = "${custom.schedule.time}")
    public void unblockAccount() {
        String url = "http://localhost:8083/api/unblock/account";
        RestTemplate restTemplate = new RestTemplate();
        ResponseUnblock response;
        List<Account> accounts = accountService.getAccountsByStatusAndLimit(AccountStatusEnum.ARRESTED, scheduleConfig.getAmountAccount());
        log.info("unblockAccount(): amount arrested account is {}", accounts.size());
        if (accounts.isEmpty()) {
            log.info("unblockAccount(): stop method, because accounts.size() = 0");
            return;
        }
        RequestUnblock requestUnblock = RequestUnblock.builder()
                .ids(accounts.stream().map(a -> a.getAccountId().toString()).toList())
                .build();
        try {
            response = restTemplate.postForEntity(url, requestUnblock, ResponseUnblock.class).getBody();
        } catch (RestClientException e) {
            log.error("unblockAccount(): RestTemplate fail: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        for (var id : response.getMap().keySet()) {
            if (response.getMap().get(id) == UnblockStatusEnum.UNBLOCKED) {
                accountService.accountUpdateStatus(id);
            }
        }
    }

}
