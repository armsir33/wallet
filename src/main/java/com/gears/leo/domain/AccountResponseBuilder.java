package com.gears.leo.domain;

public class AccountResponseBuilder {

    private String accountId;
    private long balance;
    private String status;
    private String description;

    public static AccountResponseBuilder create() {
        return new AccountResponseBuilder();
    }

    public AccountResponse build() {
        final AccountResponse response = new AccountResponse();
        response.setAccountId(accountId);
        response.setBalance(balance);
        response.setStatus(status);
        response.setDescription(description);
        return response;
    }

    public AccountResponseBuilder withAccountId(String accId) {
        this.accountId = accId;
        return this;
    }

    public AccountResponseBuilder withBalance(long balanc) {
        this.balance = balanc;
        return this;
    }

    public AccountResponseBuilder withStatus(String statu) {
        this.status = statu;
        return this;
    }

    public AccountResponseBuilder withDescription(String desc) {
        this.description = desc;
        return this;
    }

}
