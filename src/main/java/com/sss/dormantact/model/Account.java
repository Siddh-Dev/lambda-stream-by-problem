package com.sss.dormantact.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Account {
    String accountId;
    String customerId;
    LocalDate openedOn;
    boolean isClosed;
}
