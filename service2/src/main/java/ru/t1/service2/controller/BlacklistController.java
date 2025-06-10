package ru.t1.service2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.core.model.BlacklistRequest;
import ru.t1.core.model.BlacklistResponse;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.service2.service.BlacklistService;

@RestController
@Slf4j
@RequestMapping("/blacklist")
public class BlacklistController {
    @Autowired
    private BlacklistService blacklistService;

    @PostMapping("/verification")
    public BlacklistResponse verificationClient(@RequestBody BlacklistRequest blacklistRequest) {
        log.info("A request for verification of the clientId {} has been received", blacklistRequest.getClientId());

        StatusClientEnum statusClient = blacklistService.clientVerification(blacklistRequest.getClientId());

        return BlacklistResponse.builder()
                .clientId(blacklistRequest.getClientId())
                .clientStatus(statusClient)
                .build();
    }
}
