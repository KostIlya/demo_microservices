package ru.t1.demo_t1.util;

import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.TimeLimitExceedLog;
import ru.t1.demo_t1.model.dto.TimeLimitExceedLogDTO;

@Component
public class TimeLimitExceedLogMapper {
    public TimeLimitExceedLog toEntity(TimeLimitExceedLogDTO timeLimitExceedLogDTO) {
        return TimeLimitExceedLog.builder()
                .executionTime(timeLimitExceedLogDTO.getExecutionTime())
                .limitTime(timeLimitExceedLogDTO.getLimitTime())
                .dateTime(timeLimitExceedLogDTO.getDateTime())
                .methodSignature(timeLimitExceedLogDTO.getMethodSignature())
                .build();
    }

    public TimeLimitExceedLogDTO toDto(TimeLimitExceedLog timeLimitExceedLog) {
        return TimeLimitExceedLogDTO.builder()
                .executionTime(timeLimitExceedLog.getExecutionTime())
                .limitTime(timeLimitExceedLog.getLimitTime())
                .dateTime(timeLimitExceedLog.getDateTime())
                .methodSignature(timeLimitExceedLog.getMethodSignature())
                .build();
    }
}
