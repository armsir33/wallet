package com.gears.leo.infrastructure;

import static com.gears.leo.domain.AnswerCode.BALANCE_NOT_ENOUGH;
import static com.gears.leo.domain.AnswerCode.INVALID_TRANSACTION_AMOUNT;
import static com.gears.leo.domain.AnswerCode.INVALID_TRANSACTION_TYPE;
import static com.gears.leo.domain.AnswerCode.INVALID_ACCOUNT_ID;
import static com.gears.leo.domain.AnswerCode.ACCOUNT_NOT_FOUND;
import static com.gears.leo.domain.AnswerCode.DUPLICATED_TRANSACTION_ID;
import static com.gears.leo.domain.AnswerCode.INVALID_TRANSACTION_ID;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gears.leo.domain.Account;
import com.gears.leo.domain.Transaction;
import com.gears.leo.domain.TransactionType;
import com.gears.leo.domain.WalletHandleService;

public class WalletHandleServiceImpl implements WalletHandleService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletHandleServiceImpl.class);

    // Map(id, account)
    private static ConcurrentMap<String, Account> repository;

    private static Set<String> txTracingPool;

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private WalletHandleServiceImpl() {
        txTracingPool = Collections.synchronizedSet(new HashSet<String>());
    }

    public static WalletHandleServiceImpl create() {
        initDb();
        return new WalletHandleServiceImpl();
    }

    private static void initDb() {
        List<Account> accounts;
        try {
            accounts = MAPPER.readValue(WalletHandleServiceImpl.class.getClassLoader().getResource("db.json"), new TypeReference<List<Account>>() {});
        } catch (Exception e) {
            LOG.error("Failed to load account data from db.json");
            throw new RuntimeException("Failed to load account data from db.json", e);
        }
        repository = accounts.stream().collect(Collectors.toConcurrentMap(Account::getId, account -> account));
    }

    @Override
    public void updateAccount(Transaction transaction) {
        Validate.notNull(transaction, "Valid transaction required");
        final String txId = Optional.of(transaction).map(Transaction::getTransactionId).orElseThrow(() -> new AnswerCodeException(INVALID_TRANSACTION_ID));
        verifyAndUpdateTxId(txId);
        final String accountId = Optional.ofNullable(transaction).map(Transaction::getAccountId).orElseThrow(() -> new AnswerCodeException(INVALID_ACCOUNT_ID));
        final long amount = Optional.ofNullable(transaction).map(Transaction::getAmount).orElseThrow(() -> new AnswerCodeException(INVALID_TRANSACTION_AMOUNT));
        final String txTypeStr = Optional.ofNullable(transaction).map(Transaction::getTransactionType).orElseThrow(() -> new AnswerCodeException(INVALID_TRANSACTION_TYPE));
        updateBalance(accountId, amount, txTypeStr);
    }

    private void updateBalance(final String userId, final long amount, final String txTypeStr) {
        final TransactionType txType = TransactionType.fromName(txTypeStr).orElseThrow(() -> new AnswerCodeException(INVALID_TRANSACTION_TYPE));
        final Account account = Optional.ofNullable(repository.get(userId)).orElseThrow(() -> new AnswerCodeException(ACCOUNT_NOT_FOUND));
        long balance = account.getBalance();
        if (txType == TransactionType.DEBIT) {
            if (balance >= amount) {
                account.withdraw(amount);
            } else {
                throw new AnswerCodeException(BALANCE_NOT_ENOUGH);
            }
        } else {
            account.deposit(amount);
        }
    }

    private void verifyAndUpdateTxId(final String txId) {
        if (txTracingPool.contains(txId)) {
            throw new AnswerCodeException(DUPLICATED_TRANSACTION_ID);
        }
        txTracingPool.add(txId);
    }

    @Override
    public long balance(String accountId) {
        Validate.notEmpty(accountId, "Valid accountId required");
        if (!repository.containsKey(accountId)) {
            LOG.warn(ACCOUNT_NOT_FOUND.getDescription() + " with " + accountId);
            throw new AnswerCodeException(ACCOUNT_NOT_FOUND);
        }
        return repository.get(accountId).getBalance();
    }

    @Override
    public List<String> listAccounts() {
        return repository.values().stream().map(Account::getId).collect(Collectors.toList());
    }

}
