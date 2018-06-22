package com.gears.leo.domain;

import java.util.EnumSet;
import java.util.Optional;

public enum TransactionType {
    DEBIT("debit"), CREDIT("credit");

    String name;

    private TransactionType(final String name) {
        this.name = name;
    }

    public static Optional<TransactionType> fromName(final String name) {
        return EnumSet.allOf(TransactionType.class).stream().filter(n -> n.getName().equals(name)).findFirst();
    }

    public String getName() {
        return name;
    }

}
