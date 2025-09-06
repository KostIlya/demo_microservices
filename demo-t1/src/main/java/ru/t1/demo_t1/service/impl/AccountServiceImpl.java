package ru.t1.demo_t1.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.demo_aspect_starter.aop.annotation.CountArrestedAccount;
import ru.t1.demo_t1.aop.annotation.Cached;
import ru.t1.demo_aspect_starter.aop.annotation.Metric;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.mapper.AccountRequestMapper;
import ru.t1.demo_t1.mapper.AccountResponseMapper;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.dto.AccountRequestDTO;
import ru.t1.demo_t1.model.dto.AccountResponseDTO;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.repository.AccountRepository;
import ru.t1.demo_t1.repository.TransactionRepository;
import ru.t1.demo_t1.service.AccountService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final AccountResponseMapper accountResponseMapper;
    @Autowired
    private final AccountRequestMapper accountRequestMapper;

    @Override
    @Metric
    @Cached
    public List<AccountResponseDTO> getAccounts() {
        log.info("Getting accounts");
        return accountRepository.findAll().stream()
                .map(accountResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Metric
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NoEntityException("Счета с id " + id + "  не существует."));
        return AccountResponseDTO.builder()
                .type(account.getType())
                .clientId(account.getClient().getClientId())
                .clientFirstName(account.getClient().getFirstName())
                .clientLastName(account.getClient().getLastName())
                .balance(account.getBalance())
                .build();
    }

    @Override
    @Metric
    @Cached(key = "clientId")
    public AccountResponseDTO getAccountByClientId(UUID clientId) {
        Account account = accountRepository.findByClientId(clientId).orElseThrow(() ->
                new NoEntityException("Счета с client_id " + clientId + " не существует."));
        return AccountResponseDTO.builder()
                .type(account.getType())
                .clientId(account.getClient().getClientId())
                .clientFirstName(account.getClient().getFirstName())
                .clientLastName(account.getClient().getLastName())
                .balance(account.getBalance())
                .build();
    }

    @Override
    @Metric
    public Account getAccountByAccountId(UUID accountId) throws NoEntityException {
        Account account = accountRepository.findByAccountId(accountId).orElseThrow(() ->
                new NoEntityException("Счета с accountId " + accountId + " не существует."));
        return account;
    }

    @Override
    public void update(Account account) {
        if (accountRepository.findById(account.getId()).isEmpty()) {
            log.error("Account with id {} doesn't exist", account.getAccountId());
            throw new NoEntityException("Account with id " + account.getAccountId() + " doesn't exist");
        }
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccountsByStatusAndLimit(AccountStatusEnum status, Long limit) {
        return accountRepository.findByStatusAndLimit(status, limit);
    }

    @Override
    @Metric
    public void createAccount(AccountRequestDTO accountRequestDTO) {
        Account account = accountRequestMapper.toEntity(accountRequestDTO);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    @Metric
    public void deleteAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NoEntityException("Счета с id " + id + "  не существует."));

        transactionRepository.deleteAll(account.getTransactions());

        accountRepository.delete(account);
    }

    @Override
    @Metric
    public void updateAccount(Long id, AccountRequestDTO accountRequestDTO) {
        accountRepository.findById(id).orElseThrow(() ->
                new NoEntityException("Счета с id " + id + "  не существует."));
        Account account = accountRequestMapper.toEntity(accountRequestDTO);
        account.setId(id);
        accountRepository.save(account);
    }
    @CountArrestedAccount(decrease = true)
    @Override
    public void accountUpdateStatus(String id) {
        Account account = getAccountByAccountId(UUID.fromString(id));
        account.setStatus(AccountStatusEnum.OPEN);
        update(account);
        log.info("Account with id {} updated", account);
    }
    @Override
    @CountArrestedAccount
    public void setStatusArrestedAccount(Account account) {
        log.info("setStatusArrestedAccount");
        account.setStatus(AccountStatusEnum.ARRESTED);
    }
}
