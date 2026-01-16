package com.sss.atmcashreconciliation.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class AtmCashPosition {
    String atmId;
    BigDecimal openingBalance;
}
