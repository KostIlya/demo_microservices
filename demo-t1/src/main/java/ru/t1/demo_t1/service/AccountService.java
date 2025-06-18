package ru.t1.demo_t1.service;

import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.dto.AccountRequestDTO;
import ru.t1.demo_t1.model.dto.AccountResponseDTO;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountResponseDTO> getAccounts();
    AccountResponseDTO getAccountById(Long id);
    AccountResponseDTO getAccountByClientId(UUID clientId);
    Account getAccountByAccountId(UUID accountId) throws NoEntityException;
    List<Account> getAccountsByStatusAndLimit(AccountStatusEnum status, Long limit);
    void createAccount(AccountRequestDTO account);
    void deleteAccountById(Long id);
    void updateAccount(Long id, AccountRequestDTO accountRequestDTO);
    void update(Account account);
    void accountUpdateStatus(String id);
    void setStatusArrestedAccount(Account account);
}
