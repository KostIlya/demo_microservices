package ru.t1.demo_t1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.demo_aspect_starter.aop.annotation.LogDataSourceError;
import ru.t1.demo_t1.model.dto.TransactionDTO;
import ru.t1.demo_t1.service.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(path = "transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @LogDataSourceError
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        log.info("Getting transactions");
//        throw new NoEntityException("Ошибка получения транзакций");
        List<TransactionDTO> transactions = transactionService.getTransactions();
        log.info("Получено {} транзакция.", transactions.size());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/by_account/{account_id}")
    @LogDataSourceError
    public ResponseEntity<List<TransactionDTO>> getByAccountId(@PathVariable String account_id) {
        log.info("Find transaction by account id {}", account_id);
//        throw new RuntimeException("Проверка ошибки в TransactionController.getByAccountId(..)");
        return ResponseEntity.ok(transactionService.getTransactionByAccountId(UUID.fromString(account_id)));
    }

    @GetMapping("/{id}")
    @LogDataSourceError
    public ResponseEntity<TransactionDTO> getById(@PathVariable Long id) {
        log.info("Find transaction by id {}", id);
//        throw new IllegalArgumentException("Проверка ошибки в TransactionController.getById(..)");
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping("save")
    @LogDataSourceError
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        log.info("Create transaction");
//        throw new RuntimeException("Проверка ошибки в TransactionController.createTransaction(..)");
        transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok("Транзация добавлена.");
    }

    @PutMapping("update/{id}")
    @LogDataSourceError
    public ResponseEntity<String> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        log.info("Update transaction");
        //throw new RuntimeException("Проверка ошибки в TransactionController.updateTransaction(..)");
        transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.ok("Транзакция обновлена.");
    }

    @DeleteMapping("delete/{id}")
    @LogDataSourceError
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        log.info("Delete transaction");
//        throw new RuntimeException("Проверка ошибки в TransactionController.deleteTransaction(..)");
        transactionService.deleteTransactionById(id);
        return ResponseEntity.ok("Транзакция удалена.");
    }
}
