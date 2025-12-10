package com.sss.service;

import com.sss.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class MonthlyStmt {

    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4400.00),  "UPI"),
                new Transaction("A12", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(4000),  "UPI"),
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(-200),  "NEFT"),
                new Transaction("A12", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(-100),  "UPI"),
                new Transaction("A11", LocalDateTime.of(2025, 03, 05, 15, 22), BigDecimal.valueOf(-100),  "UPI")
        );

        System.out.println(transactions);


        //Total credits by AccountID

        Map<String, BigDecimal> totalCreditByAcct = transactions.stream()
                .filter(t -> t.getAmount().signum() > 0)
                .collect(toMap(Transaction::getAccountId,
                        Transaction::getAmount,
                        BigDecimal::add));

        System.out.println(totalCreditByAcct);

    }
}
