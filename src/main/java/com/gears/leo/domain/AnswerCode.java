package com.gears.leo.domain;

public enum AnswerCode {

    BALANCE_NOT_ENOUGH(1, "Balance not enought"),
    INVALID_TRANSACTION_AMOUNT(2, "Invalid transaction amount"),
    INVALID_TRANSACTION_TYPE(3, "Invalid transaction type"),
    INVALID_ACCOUNT_ID(3, "Invalid account ID"),
    ACCOUNT_NOT_FOUND(4, "Account not found"),
    DUPLICATED_TRANSACTION_ID(5, "Duplicated transaction ID"),
    INVALID_TRANSACTION_ID(6, "Invalid transaction ID");

    Integer id;
    String description;

    private AnswerCode(final Integer id, final String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
