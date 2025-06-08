package ru.t1.demo_t1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.demo_t1.aop.annotation.Cached;
import ru.t1.demo_t1.aop.annotation.Metric;
import ru.t1.demo_t1.util.ClientMapper;
import ru.t1.demo_t1.model.dto.ClientDTO;
import ru.t1.demo_t1.repository.ClientRepository;
import ru.t1.demo_t1.service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
}
