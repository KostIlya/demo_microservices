package ru.t1.demo_t1.mapper;

import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.dto.ClientDTO;

@Component
public class ClientMapper {
    public Client toEntity(ClientDTO clientDto) {
        return Client.builder()
                .lastName(clientDto.getLastName())
                .middleName(clientDto.getMiddleName())
                .firstName(clientDto.getFirstName())
                .clientId(clientDto.getClientId())
                .build();
    }

    public ClientDTO toDto(Client client) {
        return ClientDTO.builder()
                .clientId(client.getClientId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .build();
    }
}
