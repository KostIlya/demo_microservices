package ru.t1.service3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.core.model.RequestUnblock;
import ru.t1.core.model.ResponseUnblock;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.core.model.enums.UnblockStatusEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/unblock")
@Slf4j
public class UnblockController {
    @PostMapping("/client")
    public ResponseEntity<ResponseUnblock> clientUnblock(@RequestBody RequestUnblock requestUnblock ) {
        ResponseUnblock response = new ResponseUnblock();
        Map<String, UnblockStatusEnum> map = new HashMap<>();
        for (var id: requestUnblock.getIds()) {
            if (new Random().nextBoolean()) {
                map.putIfAbsent(id, UnblockStatusEnum.UNBLOCKED);
            } else {
                map.putIfAbsent(id, UnblockStatusEnum.BLOCKED);
            }
        }
        response.setMap(map);
        long count = map.values().stream()
                .filter(v -> v.equals(UnblockStatusEnum.UNBLOCKED))
                .count();
        log.info("clientUnblock(): count of unblocked clients: {}", count);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/account")
    public ResponseEntity<ResponseUnblock> accountUnblock(@RequestBody RequestUnblock requestUnblock ) {
        ResponseUnblock response = new ResponseUnblock();
        Map<String, UnblockStatusEnum> map = new HashMap<>();
        for (var id: requestUnblock.getIds()) {
            if (new Random().nextBoolean()) {
                map.putIfAbsent(id, UnblockStatusEnum.UNBLOCKED);
            } else {
                map.putIfAbsent(id, UnblockStatusEnum.BLOCKED);
            }
        }
        response.setMap(map);
        long count = map.values().stream()
                .filter(v -> v.equals(UnblockStatusEnum.UNBLOCKED))
                .count();
        log.info("accountUnblock(): count of unblocked accounts: {}", count);
        return ResponseEntity.ok(response);
    }
}
