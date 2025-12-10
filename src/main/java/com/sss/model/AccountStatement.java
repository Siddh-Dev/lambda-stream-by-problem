package com.sss.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class AccountStatement {

    BigDecimal totalCredits;
    BigDecimal totalDebits;
    BigDecimal netChange;
    Map<String, Long> txnCountByChannel;
    List<Transaction> top5LargestDebits;


}
