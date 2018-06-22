package com.gears.leo.domain;

public class TransactionResponseBuilder {

    private String transactionId;
    private String status;
    private String description;

    public static TransactionResponseBuilder create() {
        return new TransactionResponseBuilder();
    }

    public TransactionResponse build() {
        final TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transactionId);
        response.setStatus(status);
        response.setDescription(description);
        return response;
    }

    public TransactionResponseBuilder withTransactionId(String txId) {
        this.transactionId = txId;
        return this;
    }

    public TransactionResponseBuilder withStatus(String sstatus) {
        this.status = sstatus;
        return this;
    }

    public TransactionResponseBuilder withDescription(String desc) {
        this.description = desc;
        return this;
    }

}
