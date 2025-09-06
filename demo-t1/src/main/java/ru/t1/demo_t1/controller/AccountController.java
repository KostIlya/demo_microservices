package ru.t1.demo_t1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.demo_aspect_starter.aop.annotation.LogDataSourceError;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.model.dto.AccountRequestDTO;
import ru.t1.demo_t1.model.dto.AccountResponseDTO;
import ru.t1.demo_t1.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    @LogDataSourceError
    public ResponseEntity<List<AccountResponseDTO>> getAccounts() {
        log.info("Getting accounts");
        throw new NoEntityException("Ошибка получения счетов");
//        List<AccountResponseDTO> accounts = accountService.getAccounts();
//        log.info("Получено {} аккаунтов.", accounts.size());
//        return ResponseEntity.ok(accounts);
    }

    @PostMapping("save")
    @LogDataSourceError
    public ResponseEntity<String> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        log.info("Create account");
//        throw new RuntimeException("Ошибка создания счетов");
        accountService.createAccount(accountRequestDTO);
        return ResponseEntity.ok("Account added");
    }

    @GetMapping("/{id}")
    @LogDataSourceError
    public ResponseEntity<AccountResponseDTO> findById(@PathVariable Long id) {
        log.info("Find account by id {}", id);
//        throw new NoEntityException("Не существует счета с id " + id);
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/by_client/{client_id}")
    @LogDataSourceError
    public ResponseEntity<AccountResponseDTO> findByClientId(@PathVariable UUID client_id) {
        log.info("Find account by client id {}", client_id);
//        throw new RuntimeException("Ошибка AccountController.findByClientId(..)");
        return ResponseEntity.ok(accountService.getAccountByClientId(client_id));
    }

    @PutMapping("update/{id}")
    @LogDataSourceError
    public ResponseEntity<String> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDTO accountRequestDTO) {
        log.info("Update account by id {}", id);
//        throw new RuntimeException("Ошибка AccountController.updateAccount(..)");
        accountService.updateAccount(id, accountRequestDTO);
        return ResponseEntity.ok("Аккаунт обновлен.");
    }

    @DeleteMapping("delete/{id}")
    @LogDataSourceError
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        log.info("Delete account by id {}", id);
//        throw new RuntimeException("Ошибка AccountController.deleteAccount(..)");
        accountService.deleteAccountById(id);
        return ResponseEntity.ok("Аккаунт удален.");
    }
}
