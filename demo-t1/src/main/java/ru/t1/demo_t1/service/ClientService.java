package ru.t1.demo_t1.service;

import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.dto.ClientDTO;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDTO> getClients();
    Client getClientByClientId(UUID clientId);
    void update(Client client);
}
