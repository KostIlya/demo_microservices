package ru.t1.demo_t1.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionEvent {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("amount")
    private BigDecimal amount;
}
