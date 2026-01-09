package com.sss.dormantact.service;

/*For a given LocalDate referenceDate, find all accounts that have:

        No transactions in last 12 months, AND
        More than 1 year old, AND
        Not closed*/


import com.sss.dormantact.model.Account;
import com.sss.dormantact.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FindDormantAccounts {

    public static void main(String[] args) {

        //Account list

       List<Account> accounts = List.of(
                Account.builder().accountId("A11").isClosed(false).
                        openedOn(LocalDate.of(2024, 02, 12)).customerId("CS1").build(),
                Account.builder().accountId("A12").isClosed(false).
                        openedOn(LocalDate.of(2022, 03, 12)).customerId("CS2").build(),
                Account.builder().accountId("A13").isClosed(true).
                        openedOn(LocalDate.of(2024, 04, 12)).customerId("CS3").build(),
                Account.builder().accountId("A14").isClosed(false).
                        openedOn(LocalDate.of(2024, 05, 12)).customerId("CS4").build()
               /* Account.builder().accountId("A15").isClosed(false).
                        openedOn(LocalDate.of(2024, 06, 12)).customerId("CS5").build(),
                Account.builder().accountId("A16").isClosed(true).
                        openedOn(LocalDate.of(2024, 11, 12)).customerId("CS6").build(),
                Account.builder().accountId("A17").isClosed(true).
                        openedOn(LocalDate.of(2025, 03, 12)).customerId("CS7").build(),
                Account.builder().accountId("A18").isClosed(true).
                        openedOn(LocalDate.of(2025, 04, 12)).customerId("CS8").build()*/

        );

       List<Transaction> transactions = List.of(

               //A11
               Transaction.builder().accountId("A11").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2025, 05, 22, 20, 12, 10)).build(),
               Transaction.builder().accountId("A11").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2025, 05, 25, 20, 12, 10)).build(),
               Transaction.builder().accountId("A11").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2025, 04, 28, 20, 12, 10)).build(),
               Transaction.builder().accountId("A11").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2025, 03, 30, 20, 12, 10)).build(),

               //A12
               Transaction.builder().accountId("A12").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2023, 05, 22, 20, 12, 10)).build(),
               Transaction.builder().accountId("A12").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2023, 05, 20, 20, 12, 10)).build(),
               Transaction.builder().accountId("A12").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2023, 05, 18, 20, 12, 10)).build(),
               Transaction.builder().accountId("A12").amount(BigDecimal.valueOf(22.00)).
                       timestamp(LocalDateTime.of(2023, 04, 22, 20, 12, 10)).build()

       );


       //Reference Date
        LocalDate referenceDate = LocalDate.now();
        LocalDate twelveMonthsAgo = referenceDate.minusMonths(12);
        LocalDate oneYearAgo     = referenceDate.minusYears(1);


        //Step1: Pre-compute active accounts
        Set<String> activeAccountIds = transactions.stream()
                .filter(tx -> !tx.getTimestamp().toLocalDate().isBefore(twelveMonthsAgo))
                .map(tx -> tx.getAccountId())
                .collect(Collectors.toSet());

        //print active accounts
        System.out.println("Active account Ids: " + activeAccountIds);

        //Step2: filter dormant accounts
        List<String> dormantAccountIds = accounts.stream()
                .filter(acc -> !acc.isClosed()) // not closed
                .filter(acc -> acc.getOpenedOn().isBefore(oneYearAgo)) // more than 1 year old
                .filter(acc -> !activeAccountIds.contains(acc.getAccountId())) // no tx in last 12 months
                .map(acc -> acc.getAccountId())
                .collect(Collectors.toList());

        System.out.println("Dormant account Ids: " + dormantAccountIds);

    }

}
