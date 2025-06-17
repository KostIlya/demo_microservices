package ru.t1.demo_t1.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.demo_t1.aop.annotation.Cached;
import ru.t1.demo_t1.aop.annotation.Metric;
import ru.t1.demo_t1.exception.NoEntityException;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.mapper.ClientMapper;
import ru.t1.demo_t1.model.dto.ClientDTO;
import ru.t1.demo_t1.repository.ClientRepository;
import ru.t1.demo_t1.service.ClientService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientMapper clientMapper;

    @Override
    @Metric
    @Cached
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cached
    public Client getClientByClientId(UUID clientId) {
        return clientRepository.findByClientId(clientId).orElseThrow(()->
                new NoEntityException("Client with clientId " + clientId + " doesn't exist"));
    }

    @Override
    public void update(Client client) {
        if (clientRepository.findById(client.getId()).isEmpty()) {
            log.error("Client with id {} doesn't exist", client.getClientId());
            throw new NoEntityException("Client with id " + client.getClientId() + " doesn't exist");
        }

        clientRepository.save(client);
    }

}
