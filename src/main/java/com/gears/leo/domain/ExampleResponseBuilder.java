package com.gears.leo.domain;

public class ExampleResponseBuilder {

    private String transactionId;
    private String orderId;
    private String accountId;
    private String transactionDate;
    private String transactionType;
    private long amount;
    private String currency;

    public static ExampleResponseBuilder create() {
        return new ExampleResponseBuilder();
    }

    public ExampleResponse build() {
        final ExampleResponse response = new ExampleResponse();
        response.setTransactionId(transactionId);
        response.setOrderId(orderId);
        response.setAccountId(accountId);
        response.setTransactionDate(transactionDate);
        response.setTransactionType(transactionType);
        response.setAmount(amount);
        response.setCurrency(currency);
        return response;
    }

    public ExampleResponseBuilder withTransactionId(String txId) {
        this.transactionId = txId;
        return this;
    }

    public ExampleResponseBuilder withOrderId(String oid) {
        this.orderId = oid;
        return this;
    }

    public ExampleResponseBuilder withAccountId(String accId) {
        this.accountId = accId;
        return this;
    }

    public ExampleResponseBuilder withTransactionDate(String date) {
        this.transactionDate = date;
        return this;
    }

    public ExampleResponseBuilder withTransactionType(String txType) {
        this.transactionType = txType;
        return this;
    }

    public ExampleResponseBuilder withAmount(long amt) {
        this.amount = amt;
        return this;
    }

    public ExampleResponseBuilder withCurrency(String curr) {
        this.currency = curr;
        return this;
    }
}
