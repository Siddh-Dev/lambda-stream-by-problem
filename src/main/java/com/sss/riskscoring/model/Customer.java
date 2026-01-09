package com.sss.riskscoring.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    String id;
    String name;
    String segment; // "RETAIL", "WEALTH", "CORPORATE"
}
