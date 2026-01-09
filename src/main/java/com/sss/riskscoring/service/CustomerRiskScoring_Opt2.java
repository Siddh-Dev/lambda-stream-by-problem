package com.sss.riskscoring.service;

import com.sss.riskscoring.model.Customer;
import com.sss.riskscoring.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerRiskScoring_Opt2 {

    public static void main(String[] args) {


        List<Transaction> transactions = List.of(
                //A11
                Transaction.builder().customerId("C11").
                        amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2025, 05, 22, 20, 12, 10)).
                        counterpartyCountry("Syria").build(),
                Transaction.builder().customerId("C11").amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2025, 05, 25, 20, 12, 10)).
                        counterpartyCountry("Syria").build(),
                Transaction.builder().customerId("C11").amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2025, 04, 28, 20, 12, 10)).
                        counterpartyCountry("India").build(),
                Transaction.builder().customerId("C11").amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2025, 03, 30, 20, 12, 10)).
                        counterpartyCountry("US").build(),

                //A12
                Transaction.builder().customerId("C12").amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2023, 05, 22, 20, 12, 10)).
                        counterpartyCountry("UK").build(),
                Transaction.builder().customerId("C12").amount(BigDecimal.valueOf(560000.00)).
                        timestamp(LocalDateTime.of(2023, 05, 20, 20, 12, 10)).
                        counterpartyCountry("UK").build(),
                Transaction.builder().customerId("C12").amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2023, 05, 18, 20, 12, 10)).
                        counterpartyCountry("US").build(),
                Transaction.builder().customerId("C12").amount(BigDecimal.valueOf(22.00)).
                        timestamp(LocalDateTime.of(2023, 04, 22, 20, 12, 10)).
                        counterpartyCountry("Venezuela").build()

        );

        Set<String> highRiskCountries = Set.of("Iran", "North Korea", "Myanmar", "Syria", "Venezuela");



        //Group Trx by Customer
        Map<String, List<Transaction>> trxGrpByCust =  transactions.stream()
                .collect(Collectors.groupingBy(tx -> tx.getCustomerId()));

        System.out.println("trxGrpByCust:::" + trxGrpByCust);


        List<Customer> customers = List.of(
                Customer.builder().id("C11").name("John").segment("WEALTH").build(),
                Customer.builder().id("C12").name("John").segment("CORPORATE").build()
                );
        System.out.println(customers);


        //*********Compute Risk scoring********//

        Map<String, Integer> customerRiskScore =
                customers.stream()
                        .collect(Collectors.toMap(customer -> customer.getId(),
                                customer -> {
                                    List<Transaction> trxByCustomer =
                                    trxGrpByCust.getOrDefault(customer.getId(), List.of());

                                    int score = 0;

                                    //Rule 1: Any transaction > 500,000
                                    if (trxByCustomer.stream()
                                            .anyMatch(tx -> tx.getAmount().compareTo(BigDecimal.valueOf(500000)) > 0)){
                                        score++;
                                    }

                                    //Rule 2: High rsk countries transaction
                                    if (trxByCustomer.stream()
                                            .anyMatch(tx -> highRiskCountries.contains(tx.getCounterpartyCountry()))) {
                                        score++;
                                    }

                                    //Rule 3: More than 10 transactions in any month
                                    Map<YearMonth, List<Transaction>> monthwiseTrx = trxByCustomer.stream()
                                            .collect(Collectors.groupingBy(tx -> YearMonth.from(tx.getTimestamp())));

                                    boolean highFrequency = monthwiseTrx.values().stream().anyMatch(monthTrx -> monthTrx.size() > 10);
                                    if (highFrequency) {
                                        score++;
                                    }


                                    return score;
                        }));

        System.out.println("Customer risk scoring:::" + customerRiskScore);

    }
}
