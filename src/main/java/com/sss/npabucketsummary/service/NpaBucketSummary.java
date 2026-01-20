package com.sss.npabucketsummary.service;

import com.sss.npabucketsummary.model.LoanAccount;
import com.sss.npabucketsummary.model.NpaBucket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NpaBucketSummary {

    public static void main(String[] args) {

        List<LoanAccount> loanAccounts = List.of(
                LoanAccount.builder().loanId("L1").customerId("C1").productType("HOME_LOAN")
                  .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(25) .build(),
                LoanAccount.builder().loanId("L2").customerId("C2").productType("HOME_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(95) .build(),
                LoanAccount.builder().loanId("L3").customerId("C3").productType("HOME_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(50) .build(),
                LoanAccount.builder().loanId("L4").customerId("C4").productType("HOME_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(60) .build(),


                LoanAccount.builder().loanId("L5").customerId("C5").productType("PERSONAL_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(25) .build(),
                LoanAccount.builder().loanId("L6").customerId("C6").productType("PERSONAL_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(45) .build(),
                LoanAccount.builder().loanId("L7").customerId("C7").productType("PERSONAL_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(50) .build(),
                LoanAccount.builder().loanId("L8").customerId("C8").productType("PERSONAL_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(92) .build(),
                LoanAccount.builder().loanId("L9").customerId("C9").productType("PERSONAL_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(95) .build(),


                LoanAccount.builder().loanId("L10").customerId("C10").productType("CAR_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(25) .build(),
                LoanAccount.builder().loanId("L11").customerId("C11").productType("CAR_LOAN")
                        .principalOutstanding(BigDecimal.valueOf(25000.00)).daysPastDue(95) .build()

        );

        Map<String, Map<NpaBucket, BigDecimal>> npaBucketSummary = loanAccounts.stream()
                .collect(Collectors.groupingBy(loanAct -> loanAct.getProductType(),
                        Collectors.groupingBy(loanAccount -> {
                                    if (loanAccount.getDaysPastDue() < 30)
                                        return NpaBucket.STANDARD;
                                    else if (loanAccount.getDaysPastDue() >= 30 && loanAccount.getDaysPastDue() < 60)
                                        return NpaBucket.SMA1;
                                    else if (loanAccount.getDaysPastDue() >= 60 && loanAccount.getDaysPastDue() < 90)
                                        return NpaBucket.SMA2;
                                    else
                                        return NpaBucket.NPA;
                                },
                                Collectors.reducing(BigDecimal.ZERO, LoanAccount::getPrincipalOutstanding, BigDecimal::add))
                ));


        System.out.println("npaBucketSummary:::" + npaBucketSummary);

    }
}
