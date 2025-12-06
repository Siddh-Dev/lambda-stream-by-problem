package com.sss.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class Transaction {
    String accountId;
    LocalDateTime timestamp;
    BigDecimal amount;   // +ve credit, -ve debit
    String channel;      // "ATM", "UPI", "IMPS", "NEFT", "NETBANKING"

}
