package com.sss.riskscoring.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    String customerId;
    LocalDateTime timestamp;
    BigDecimal amount;
    String counterpartyCountry;
}
