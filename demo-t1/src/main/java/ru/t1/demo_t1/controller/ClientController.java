package ru.t1.demo_t1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.t1.demo_t1.aop.annotation.LogDataSourceError;
import ru.t1.demo_t1.model.dto.ClientDTO;
import ru.t1.demo_t1.service.ClientService;

import java.util.List;

@Controller
@RequestMapping(path = "client")
@Slf4j
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping
    @LogDataSourceError
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        log.info("Получение списка клиентов.");
//        throw new NoEntityException("Клиентов не существует в БД.");
        return ResponseEntity.ok(clientService.getClients());
    }
}
