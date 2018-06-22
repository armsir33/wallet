package com.gears.leo.domain;

import java.util.List;

public interface WalletHandleService {
    void updateAccount(Transaction transaction);

    long balance(String accountId);

    List<String> listAccounts();
}
