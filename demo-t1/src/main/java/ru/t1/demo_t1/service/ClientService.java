package ru.t1.demo_t1.service;

import ru.t1.demo_t1.model.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
}
