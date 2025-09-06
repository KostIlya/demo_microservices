package ru.t1.service2.service;

import ru.t1.core.model.enums.StatusClientEnum;

import java.util.UUID;

public interface BlacklistService {
    StatusClientEnum clientVerification(UUID clientId);
}
