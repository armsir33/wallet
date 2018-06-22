package com.gears.leo.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.Validate;

public class Account {
    private String id;
    private String name;
    private AtomicLong balance;

    public Account() {
        this.balance = new AtomicLong();
    }

    public Account(String id, String name, AtomicLong balance) {
        this.id = Validate.notNull(id, "Valid id required");
        this.name = Validate.notNull(name, "Valid name required");
        this.balance = Validate.notNull(balance, "Valid balance required");
    }

    public static Account openAccount(String id, String name) {
        return new Account(id, name, new AtomicLong(0L));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBalance() {
        return balance.get();
    }

    public void setBalance(long balance) {
        this.balance.set(balance);
    }

    public void deposit(long amount) {
        this.balance.getAndAdd(amount);
    }

    public void withdraw(long amount) {
        this.balance.getAndAdd(-amount);
    }

    @Override
    public boolean equals(Object that) {
        return Objects.equals(this, that);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }
}
