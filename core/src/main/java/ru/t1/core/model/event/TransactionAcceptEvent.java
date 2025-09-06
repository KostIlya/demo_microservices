package ru.t1.core.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAcceptEvent {
    @JsonProperty("client_id")
    private UUID clientId;
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("transaction_id")
    private UUID transactionId;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("balance")
    private BigDecimal balance;
}
