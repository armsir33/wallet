package com.gears.leo.infrastructure;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gears.leo.domain.Transaction;

public class WalletHandleServiceImplTest {

    private static final ClassLoader LOADER = WalletHandleServiceImpl.class.getClassLoader();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private WalletHandleServiceImpl handlerService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        handlerService = WalletHandleServiceImpl.create();
    }

    @Test
    public void testUpdateAccountWithHappyFlow() throws Exception {
        // given
        final Transaction transaction = MAPPER.readValue(LOADER.getResource("defaultTransaction.json"), Transaction.class);
        // when
        handlerService.updateAccount(transaction);
        // then
        assertThat(900L, equalTo(handlerService.balance("A1000")));
    }

    @Test
    public void testUpdateAccountWithInvalidAccount() throws Exception {
        this.thrown.expect(AnswerCodeException.class);
        this.thrown.expectMessage("Account not found");
        // given
        final Transaction transaction = MAPPER.readValue(LOADER.getResource("defaultTransaction.json"), Transaction.class);
        transaction.setAccountId("A5000");
        // when
        handlerService.updateAccount(transaction);
    }

    @Test
    public void testUpdateAccountWithDuplicatedTx() throws Exception {
        this.thrown.expect(AnswerCodeException.class);
        this.thrown.expectMessage("Duplicated transaction ID");
        // given
        final Transaction transaction = MAPPER.readValue(LOADER.getResource("defaultTransaction.json"), Transaction.class);
        // when
        handlerService.updateAccount(transaction);
        handlerService.updateAccount(transaction);
    }

    @Test
    public void testUpdateAccountWithAccountBalanceNotEnough() throws Exception {
        this.thrown.expect(AnswerCodeException.class);
        this.thrown.expectMessage("Balance not enough");
        // given
        final Transaction transaction = MAPPER.readValue(LOADER.getResource("defaultTransaction.json"), Transaction.class);
        transaction.setAmount(1100L);
        // when
        handlerService.updateAccount(transaction);
    }

    @Test
    public void testBalance() {
        assertThat(1000L, equalTo(handlerService.balance("A1000")));
    }

    @Test
    public void testListAccounts() {
        final List<String> actual = Collections.unmodifiableList(Arrays.asList("A1000", "A2000", "A3000"));
        assertThat(actual.size(), equalTo(handlerService.listAccounts().size()));
        Assert.assertTrue(actual.containsAll(handlerService.listAccounts()));
    }
}
