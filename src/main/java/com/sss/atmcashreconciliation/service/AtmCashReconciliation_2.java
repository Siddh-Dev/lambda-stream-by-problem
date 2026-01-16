package com.sss.atmcashreconciliation.service;

import com.sss.atmcashreconciliation.model.AtmCashPosition;
import com.sss.atmcashreconciliation.model.AtmTxn;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmCashReconciliation_2 {

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
                AtmTxn.builder().atmId("ATM33").type("DEPOSIT").time(LocalDateTime.of(2025, 12, 5, 20, 25, 0))
                        .amount(BigDecimal.valueOf(500)).build(),
                AtmTxn.builder().atmId("ATM33").type("WITHDRAWAL").time(LocalDateTime.of(2025, 12, 5, 20, 25, 5))
                        .amount(BigDecimal.valueOf(1000)).build()

        );


        //Compute net transaction amount per ATM
        Map<String, BigDecimal> txnDeltaByAtm =
                transactions.stream()
                     //   .filter(txn -> !txn.getTime().isAfter(cutoffTime))
                        .collect(Collectors.groupingBy(
                                AtmTxn::getAtmId,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        txn -> "DEPOSIT".equals(txn.getType())
                                                ? txn.getAmount()
                                                : txn.getAmount().negate(),
                                        BigDecimal::add
                                )
                        ));

        //Merge opening balance with transaction delta
        Map<String, BigDecimal> atmClosingCash =
                openingBalances.stream()
                        .collect(Collectors.toMap(
                                AtmCashPosition::getAtmId,
                                ob -> ob.getOpeningBalance()
                                        .add(txnDeltaByAtm.getOrDefault(
                                                ob.getAtmId(), BigDecimal.ZERO)),
                                BigDecimal::add
                        ));

        System.out.println(atmClosingCash);
    }
}
