package ru.t1.service2.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.service2.service.BlacklistService;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class BlacklistServiceImpl implements BlacklistService {
    @Override
    public StatusClientEnum clientVerification(UUID clientId) {
        StatusClientEnum result = StatusClientEnum.ACTIVE;
        if (new Random().nextBoolean()) {
            result = StatusClientEnum.BLOCKED;
        }
        log.info("Client with client_id {}: {}", clientId, result);
        return result;
    }
}
