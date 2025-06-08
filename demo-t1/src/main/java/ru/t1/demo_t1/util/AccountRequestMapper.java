package ru.t1.demo_t1.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.dto.AccountRequestDTO;
import ru.t1.demo_t1.repository.ClientRepository;

@Component
@Slf4j
public class AccountRequestMapper {
    @Autowired
    ClientRepository clientRepository;

    public Account toEntity(AccountRequestDTO accountRequestDTO) {
        Client client = clientRepository.findByClientId(accountRequestDTO.getClientId());
        return Account.builder()
                .type(accountRequestDTO.getType())
                .balance(accountRequestDTO.getBalance())
                .client(client)
                .accountId(accountRequestDTO.getAccountId())
                .status(accountRequestDTO.getStatus())
                .build();
    }

    public AccountRequestDTO toDto(Account account) {
        return AccountRequestDTO.builder()
                .type(account.getType())
                .balance(account.getBalance())
                .accountId(account.getAccountId())
                .status(account.getStatus())
                .clientId(account.getClient().getClientId())
                .build();
    }

}
