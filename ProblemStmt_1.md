### 6. ATM Cash Reconciliation

```java
class AtmTxn {
    String atmId;
    LocalDateTime time;
    String type; // "WITHDRAWAL" or "DEPOSIT"
    BigDecimal amount;
}
class AtmCashPosition {
    String atmId;
    BigDecimal openingBalance;
}

```

**Task:**

Given:

- `List<AtmCashPosition> openingBalances`
- `List<AtmTxn> transactions` for the day

Compute **closing cash** per ATM:

```java
Map<String, BigDecimal> atmClosingCash;

```

Support:

- Apply deposits as +, withdrawals as -
- Ignore transactions beyond a given cutoff time. (Optional)