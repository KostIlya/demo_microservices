package ru.t1.core.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.t1.core.model.enums.TransactionStatusEnum;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResultEvent {
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("transaction_id")
    private UUID transactionId;
    @JsonProperty("status")
    private TransactionStatusEnum status;
}
