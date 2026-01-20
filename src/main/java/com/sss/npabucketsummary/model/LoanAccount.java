package com.sss.npabucketsummary.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanAccount {
    String loanId;
    String customerId;
    BigDecimal principalOutstanding;
    int daysPastDue; // DPD
    String productType; // "HOME_LOAN", "PERSONAL_LOAN", "CAR_LOAN"

}
