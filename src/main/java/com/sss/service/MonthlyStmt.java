package com.sss.service;

import com.sss.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MonthlyStmt {

    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4400.00),  "UPI"),
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4400.00),  "UPI"),
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4400.00),  "UPI"),
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4400.00),  "UPI"),
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4400.00),  "UPI")
        );

        System.out.println(transactions);

    }
}
