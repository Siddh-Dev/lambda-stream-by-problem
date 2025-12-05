# lambda-stream-by-problem
# This repo have practice problem statement implemeted using Java 8 lambda and Stream features

Here are **16 advanced, domain-specific problems** (Finance / Banking / Inventory / ATM) explicitly designed to make you use **Streams, Lambdas, Collectors, custom collectors, grouping, partitioning, and flatMap**.

I’ll give each problem:

- **Domain context**
- **Sample model classes**
- **Exact output / requirement**
- **Stream-focused hints** (but still problem-only, no solutions)

---

## 🏦 A. Banking / Accounts / Transactions

---

### 1. Monthly Statement Generator (Per Account)

**Domain:** Retail banking

You have:

```java
class Transaction {
    String accountId;
    LocalDateTime timestamp;
    BigDecimal amount;   // +ve credit, -ve debit
    String channel;      // "ATM", "UPI", "IMPS", "NEFT", "NETBANKING"
}

```

**Task:**

Given a `List<Transaction>` and a `YearMonth targetMonth`, generate a **monthly statement map**:

```java
Map<String, AccountStatement> // key = accountId

```

Where `AccountStatement` contains:

- `BigDecimal totalCredits`
- `BigDecimal totalDebits`
- `BigDecimal netChange`
- `Map<String, Long> txnCountByChannel` (e.g. UPI -> 10, ATM -> 2)
- `List<Transaction> top5LargestDebits` (by absolute amount)

**Stream focus:**

- `Collectors.groupingBy(accountId, ...)`
- Inner collectors for summarizing debit/credit
- `collectingAndThen` to compute `netChange`
- `Comparator.comparing` for top 5 debits

---

### 2. Dormant Account Detector

**Domain:** Compliance / Risk

```java
class Account {
    String id;
    String customerId;
    LocalDate openedOn;
    boolean isClosed;
}

class Transaction {
    String accountId;
    LocalDateTime timestamp;
    BigDecimal amount;
}

```

**Task:**

For a given `LocalDate referenceDate`, find all **accounts that have:**

1. **No transactions in last 12 months**, AND
2. **More than 1 year old**, AND
3. **Not closed**

Return:

```java
List<String> dormantAccountIds;

```

**Stream focus:**

- Group transactions by accountId and find last transaction date
- `filter` based on date conditions
- Possibly use `Map<String, Optional<LocalDateTime>>` with `maxBy`

---

### 3. Risk Scoring of Customers

```java
class Customer {
    String id;
    String name;
    String segment; // "RETAIL", "WEALTH", "CORPORATE"
}

class Transaction {
    String customerId;
    LocalDateTime timestamp;
    BigDecimal amount;
    String counterpartyCountry;
}

```

**Rule-based risk score:**

- +1 if any transaction > ₹5,00,000
- +1 if any transaction to high-risk country (list given)
- +1 if more than 50 transactions in a month
    
    Return:
    

```java
Map<String, Integer> customerRiskScore;

```

**Stream focus:**

- `groupingBy(customerId)`
- `anyMatch`, `count()`, `flatMap` logic inside `collectingAndThen`
- `Map<String, Integer>` computed only via Stream operations.

---

### 4. Interest Computation with Slabs

You have daily balance snapshots:

```java
class DailyBalance {
    String accountId;
    LocalDate date;
    BigDecimal endOfDayBalance;
}

```

**Interest rule example:**

- 0 – 1,00,000 → 3%
- 1,00,001 – 5,00,000 → 4%
- 5,00,000 → 5%

**Task:**

For each account, compute **total monthly interest** for a given `YearMonth`.

Use **Streams only** to:

- Filter by month
- Group by account
- For each day, apply slab interest, sum up monthly

**Stream focus:**

- `groupingBy(accountId)`
- Custom `map` per DailyBalance to per-day interest
- `reduce` or `Collectors.reducing` for BigDecimal

---

### 5. NPA Bucket Summary (Advanced Grouping)

```java
class LoanAccount {
    String loanId;
    String customerId;
    BigDecimal principalOutstanding;
    int daysPastDue; // DPD
    String productType; // "HOME_LOAN", "PERSONAL_LOAN", "CAR_LOAN"
}

```

Buckets (NPA stages):

- `STANDARD`: DPD < 30
- `SMA1`: 30–59
- `SMA2`: 60–89
- `NPA`: >= 90

**Task:**

Return:

```java
Map<String, Map<NpaBucket, BigDecimal>> // productType -> (bucket -> totalOutstanding)

```

**Stream focus:**

- Multi-level grouping: `groupingBy(productType, groupingBy(NpaBucket, summingBigDecimal))`
- Custom classifier function converting `daysPastDue` to `NpaBucket` enum.
- Custom collector for `BigDecimal` sum.

---

## 💳 B. ATM / Card Domain

---

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
- Ignore transactions beyond a given cutoff time.

**Stream focus:**

- Merge opening balances with stream-summed net Txn amount
- `groupingBy(atmId, reducing)` for txns
- `toMap` with merge of opening + delta

---

### 7. ATM Failure Analysis

```java
class AtmEvent {
    String atmId;
    LocalDateTime time;
    String eventType; // "SUCCESS", "DECLINED_INSUFFICIENT_FUNDS", "DECLINED_TECHNICAL"
}

```

**Task:**

Generate a report:

```java
class AtmHealthSummary {
    long totalTxns;
    long failedTxns;
    double failureRate; // 0–1
    Map<String, Long> failureCountByType;
}
Map<String, AtmHealthSummary> byAtm;

```

**Stream focus:**

- `groupingBy(atmId)`
- Within each group, derive `AtmHealthSummary` using `collectingAndThen`
- Sub-grouping by `eventType` for failure counts.

---

### 8. Debit Card Daily Limit Breach Detection

```java
class CardTxn {
    String cardId;
    LocalDateTime timestamp;
    BigDecimal amount;
    String merchantCategory; // "GROCERY", "FUEL", etc.
}

```

**Given:** per-card daily limit: `Map<String, BigDecimal> cardLimitMap`.

**Task:**

Find all **card + date** combinations where **total spending > daily limit**:

Return a list of:

```java
class LimitBreach {
    String cardId;
    LocalDate date;
    BigDecimal totalAmount;
    BigDecimal limit;
}

```

**Stream focus:**

- `groupingBy(cardId, then groupingBy(LocalDate))`
- `summingBigDecimal` style collector
- Convert grouped structure to `List<LimitBreach>` via `entrySet().stream()`

---

## 📦 C. Inventory / Warehouse Operations

---

### 9. Inventory Aging Report

```java
class InventoryItem {
    String sku;
    String warehouseId;
    LocalDate receivedDate;
    int quantity;
}

```

**Aging buckets:**

- `AGE_0_30`
- `AGE_31_90`
- `AGE_91_180`
- `AGE_180_PLUS`

**Task:**

For a `LocalDate asOf`, build:

```java
Map<String, Map<AgingBucket, Integer>> // warehouseId -> (AgingBucket -> totalQty)

```

**Stream focus:**

- Custom classifier from `(asOf - receivedDate)` to `AgingBucket`.
- Multi-level grouping with counting/summing quantity.

---

### 10. Stock Reorder Suggestion

```java
class SkuStock {
    String sku;
    String warehouseId;
    int quantityOnHand;
    int reorderLevel;
    int reorderQty;
}

```

**Task:**

Using Streams, create:

1. `List<SkuStock>` of items where `quantityOnHand < reorderLevel`
2. For each warehouse, compute **total reorder quantity** required
    
    -> `Map<String, Integer> warehouseToReorderQty`
    

**Stream focus:**

- Filtering + mapping
- `Collectors.groupingBy(warehouseId, summingInt(reorderQty))`

---

### 11. Best Seller & Slow Mover Detection

```java
class SalesTxn {
    String sku;
    LocalDate date;
    int quantitySold;
    String channel; // "ONLINE", "STORE"
}

```

**Task:**

For a given quarter:

- For each `channel`, find **top 5 best-selling SKUs** by total quantity.
- Also find **bottom 5 slow movers** (but with at least 1 sale).

**Stream focus:**

- Filter by date range
- `groupingBy(channel, then groupingBy(sku, summingInt))`
- Sorting each channel’s entries to pick top/bottom 5

---

### 12. Consolidated Inventory Across Warehouses (Merge & Group)

You have:

```java
class WarehouseStock {
    String warehouseId;
    String sku;
    int quantity;
}

```

**Task:**

- Given `List<WarehouseStock>`, compute **total quantity per SKU**.
- Return only SKUs where **total quantity < some threshold** (low stock alert).

Result:

```java
Map<String, Integer> lowStockSkus;

```

**Stream focus:**

- `groupingBy(sku, summingInt(quantity))`
- Filter out sums >= threshold
- Final `Collectors.toMap` or process of `entrySet()`.

---

## 💰 D. Generic Finance / Portfolio / Markets

---

### 13. Portfolio Performance Calculator

```java
class Trade {
    String portfolioId;
    String symbol;
    LocalDateTime time;
    int quantity;     // +buy, -sell
    BigDecimal price; // trade price
}

class MarketPrice {
    String symbol;
    BigDecimal lastTradedPrice;
}

```

**Task:**

For each `portfolioId`, compute:

- `Map<String, Position>` positions by symbol with:
    - netQuantity
    - avgBuyPrice
    - marketValue (= netQuantity * LTP)
    - unrealizedPnL (= (LTP - avgBuyPrice) * netQuantity)

**Stream focus:**

- Group by `portfolioId`, then by `symbol`
- Customized collector to derive avg price & net quantity from trades
- Join with `List<MarketPrice>` (can use a `Map<String, MarketPrice>` lookup).

---

### 14. Cash Flow Projection (Time-Series)

```java
class CashFlow {
    String projectId;
    LocalDate date;
    BigDecimal amount; // +inflow, -outflow
}

```

**Task:**

For each project, compute **cumulative cashflow over time**:

Return:

```java
Map<String, List<CumulativePoint>>
class CumulativePoint {
    LocalDate date;
    BigDecimal cumulativeAmount;
}

```

**Stream focus:**

- `groupingBy(projectId)`
- For each group: sort by date (using stream) and then use `reduce` or a custom collector to accumulate running totals
- `collectingAndThen` to transform sorted list into `List<CumulativePoint>`

---

### 15. Customer Profitability Analysis

```java
class Revenue {
    String customerId;
    LocalDate date;
    BigDecimal amount;
    String product; // "SAVINGS", "LOAN", "CREDIT_CARD"
}

class Cost {
    String customerId;
    LocalDate date;
    BigDecimal amount;
    String costType; // "OPS", "RISK", "IT"
}

```

**Task:**

For a given year:

1. Compute **total revenue** and **total cost** per customer.
2. Derive **profit = revenue - cost**.
3. Produce a sorted list of customers by descending profit, but **only for those with revenue > 0**.

**Stream focus:**

- Build two maps via Streams: `customerId -> totalRevenue`, `customerId -> totalCost`
- Merge them into a stream of `(customerId, profit)`
- Sorting and filtering in stream fashion

---

### 16. Custom Collector – Transaction Statistics

```java
class Txn {
    String id;
    BigDecimal amount;
    String type; // "CREDIT", "DEBIT"
    LocalDateTime time;
}

```

**Task:**

Create a **custom collector** to compute:

```java
class TxnStats {
    long count;
    BigDecimal totalCredit;
    BigDecimal totalDebit;
    BigDecimal maxAmount;
    BigDecimal minAmount;
}

```

Using only:

```java
TxnStats stats = txns.stream().collect(new TxnStatsCollector());

```

(Where `TxnStatsCollector` implements `Collector<Txn, ?, TxnStats>`)

**Stream focus:**

- Designing `supplier`, `accumulator`, `combiner`, `finisher`, and `characteristics`
- Handling `BigDecimal` operations in accumulator

