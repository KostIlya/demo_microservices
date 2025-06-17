package ru.t1.demo_t1.mapper;

import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.dto.AccountResponseDTO;

@Component
public class AccountResponseMapper {
//        public Account toEntity(AccountResponseDTO accountResponseDTO) {
//        return Account.builder()
//                .type(accountResponseDTO.getType())
//                .balance(accountResponseDTO.getBalance())
//                .build();
//    }
    public AccountResponseDTO toDto(Account account) {
        return AccountResponseDTO.builder()
                .clientFirstName(account.getClient().getFirstName())
                .clientLastName(account.getClient().getLastName())
                .clientId(account.getClient().getClientId())
                .balance(account.getBalance())
                .type(account.getType())
                .build();
    }
}
