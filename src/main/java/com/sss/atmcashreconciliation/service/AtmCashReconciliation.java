package com.sss.atmcashreconciliation.service;


import com.sss.atmcashreconciliation.model.AtmCashPosition;
import com.sss.atmcashreconciliation.model.AtmTxn;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmCashReconciliation {

    public static void main(String[] args) {

        List<AtmCashPosition> openingBalances =
                List.of( AtmCashPosition.builder().atmId("ATM11").openingBalance(BigDecimal.valueOf(50000)).build(),
                        AtmCashPosition.builder().atmId("ATM22").openingBalance(BigDecimal.valueOf(40000)).build(),
                        AtmCashPosition.builder().atmId("ATM33").openingBalance(BigDecimal.valueOf(220000)).build()
                );


        List<AtmTxn> transactions = List.of(
                AtmTxn.builder().atmId("ATM11").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(1500)).build(),
                AtmTxn.builder().atmId("ATM11").type("DEPOSIT").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(500)).build(),
                AtmTxn.builder().atmId("ATM11").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(10000)).build(),

                AtmTxn.builder().atmId("ATM22").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(1000)).build(),
                AtmTxn.builder().atmId("ATM22").type("DEPOSIT").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(500)).build(),
                AtmTxn.builder().atmId("ATM22").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(1000)).build(),

                AtmTxn.builder().atmId("ATM33").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(2000)).build(),
                AtmTxn.builder().atmId("ATM33").type("DEPOSIT").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(500)).build(),
                AtmTxn.builder().atmId("ATM33").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(1000)).build()

        );


        //Compute closing cash per ATM
        // Map<String, BigDecimal> atmClosingCash;

        Map<String, List<AtmTxn>> trxGroupingByAtm =  transactions.stream()
                .collect(Collectors.groupingBy(tx -> tx.getAtmId()));


        // Compute Closing Balance
        Map<String, BigDecimal> atmClosingCash
                = openingBalances.stream()
                .collect(Collectors.toMap(openingBalance -> openingBalance.getAtmId(),
                            openingBalance -> {
                                BigDecimal closingBalance = BigDecimal.ZERO;
                                List<AtmTxn> atmTrx = trxGroupingByAtm.get(openingBalance.getAtmId());

                                BigDecimal depositSum = atmTrx.stream()
                                        .filter(atmTxn -> atmTxn.getType().equals("DEPOSIT"))
                                        .collect(Collectors.reducing(
                                                BigDecimal.ZERO,
                                                AtmTxn::getAmount,
                                                BigDecimal::add
                                        ));

                                BigDecimal withdrawalSum = atmTrx.stream()
                                        .filter(atmTxn -> atmTxn.getType().equals("WITHDRAWAL"))
                                        .collect(Collectors.reducing(
                                                BigDecimal.ZERO,
                                                AtmTxn::getAmount,
                                                BigDecimal::add
                                        ));

                                closingBalance = openingBalance.getOpeningBalance().add(depositSum).subtract(withdrawalSum);
                                return closingBalance;
                            }
                         ));


        System.out.println(atmClosingCash);



    }
}
