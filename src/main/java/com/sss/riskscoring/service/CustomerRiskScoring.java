package com.sss.riskscoring.service;


import com.sss.riskscoring.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerRiskScoring {


    public static Map<String, Integer> customerRiskScoring
            = new HashMap<>();

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

        List<String> highRiskCountries = List.of("Iran", "North Korea", "Myanmar", "Syria", "Venezuela");



        Set<String> customerIdsHV = transactions.stream()
                 .filter(tx -> tx.getAmount().doubleValue() > 500000.00)
                 .map(tx -> tx.getCustomerId())
                 .collect(Collectors.toSet());



       Set<String> customerIdsHRCountries = transactions.stream()
                .filter(tx1 -> highRiskCountries.contains(tx1.getCounterpartyCountry()))
                 .map(tx -> tx.getCustomerId())
                 .collect(Collectors.toSet());

        for(String customerId: customerIdsHV)
            updateCustomerRiskScoring(customerId);
        for(String customerId: customerIdsHRCountries)
            updateCustomerRiskScoring(customerId);

        System.out.println("Customer Risk Scoring: " + customerRiskScoring);

    }

    public static void updateCustomerRiskScoring(String customerId) {

       if(customerRiskScoring.get(customerId) == null)
           customerRiskScoring.put(customerId, 1);
       else
           customerRiskScoring.put(customerId, customerRiskScoring.get(customerId) + 1);

    }


}
