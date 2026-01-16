package com.sss.atmcashreconciliation.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class AtmTxn {
    String atmId;
    LocalDateTime time;
    String type; // "WITHDRAWAL" or "DEPOSIT"
    BigDecimal amount;
}
