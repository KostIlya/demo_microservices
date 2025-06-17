package ru.t1.demo_aspect_starter.mapper;

import org.springframework.stereotype.Component;
import ru.t1.demo_aspect_starter.model.DataSourceErrorLog;
import ru.t1.demo_aspect_starter.model.dto.DataSourceErrorLogDTO;

@Component
public class DataSourceErrorLogMapper {
    public DataSourceErrorLog toEntity(DataSourceErrorLogDTO dataSourceErrorLogDTO) {
        return DataSourceErrorLog.builder()
                .stacktrace(dataSourceErrorLogDTO.getStacktrace())
                .message(dataSourceErrorLogDTO.getMessage())
                .methodSignature(dataSourceErrorLogDTO.getMethodSignature())
                .build();
    }

    public DataSourceErrorLogDTO toDto(DataSourceErrorLog dataSourceErrorLog) {
        return DataSourceErrorLogDTO.builder()
                .stacktrace(dataSourceErrorLog.getStacktrace())
                .message(dataSourceErrorLog.getMessage())
                .methodSignature(dataSourceErrorLog.getMethodSignature())
                .build();
    }
}
