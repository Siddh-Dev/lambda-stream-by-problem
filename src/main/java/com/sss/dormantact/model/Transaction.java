package com.sss.dormantact.model;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    String accountId;
    LocalDateTime timestamp;
    BigDecimal amount;
}
