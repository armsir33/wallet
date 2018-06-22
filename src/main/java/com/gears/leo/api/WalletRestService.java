package com.gears.leo.api;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gears.leo.domain.AccountResponse;
import com.gears.leo.domain.AccountResponseBuilder;
import com.gears.leo.domain.ExampleResponse;
import com.gears.leo.domain.ExampleResponseBuilder;
import com.gears.leo.domain.Transaction;
import com.gears.leo.domain.WalletHandleService;
import com.gears.leo.domain.TransactionResponse;
import com.gears.leo.domain.TransactionResponseBuilder;
import com.gears.leo.domain.TransactionType;
import com.gears.leo.infrastructure.AnswerCodeException;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WalletRestService implements ApiService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletRestService.class);

    private final WalletHandleService walletHandleService;

    public WalletRestService(final WalletHandleService walletHandleService) {
        this.walletHandleService = Validate.notNull(walletHandleService, "Valid walletHandlerService required");
    }

    @POST
    @Path("/api/account/update")
    public Response updateAccount(Transaction transaction) {
        LOG.info("updateAccount with ", transaction);
        try {
            walletHandleService.updateAccount(transaction);
            final TransactionResponse response = TransactionResponseBuilder.create().withTransactionId(transaction.getTransactionId()).withStatus(Status.OK.toString()).build();
            return Response.status(Status.OK).entity(response).build();
        } catch (AnswerCodeException ace) {
            LOG.error(ace.getMessage());
            final TransactionResponse response = TransactionResponseBuilder.create()
                    .withTransactionId(transaction.getTransactionId())
                    .withStatus(Status.BAD_REQUEST.toString())
                    .withDescription(ace.getMessage()).build();
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            final TransactionResponse response = TransactionResponseBuilder.create()
                    .withTransactionId(transaction.getTransactionId())
                    .withStatus(Status.BAD_REQUEST.toString())
                    .withDescription(e.getMessage()).build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/api/account/{accountId}/balance")
    public Response balance(@PathParam(value = "accountId") String accountId) {
        LOG.info("balance {}", accountId);
        try {
            long balance = walletHandleService.balance(accountId);
            final AccountResponse response = AccountResponseBuilder.create().withAccountId(accountId).withBalance(balance).build();
            return Response.status(Status.OK).entity(response).build();
        } catch (AnswerCodeException ace) {
            LOG.warn(ace.getMessage());
            final AccountResponse response = AccountResponseBuilder.create()
                    .withAccountId(accountId)
                    .withDescription(ace.getMessage()).build();
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            final AccountResponse response = AccountResponseBuilder.create()
                    .withStatus(Status.BAD_REQUEST.toString())
                    .withAccountId(accountId)
                    .withDescription(e.getMessage()).build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/api/accounts")
    public List<String> listAccounts() {
        LOG.info("list all accounts");
        try {
            return walletHandleService.listAccounts();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status")
    public String getStatus() {
        LOG.info("getStatus");
        return "UP";
    }

    @GET
    @Path("/example")
    public Response example() {
        LOG.info("Generating example");
        try {
            final String txId = String.valueOf(System.currentTimeMillis());
            final ExampleResponse response = ExampleResponseBuilder.create()
                    .withTransactionId(txId)
                    .withOrderId(txId).withAccountId("A1000")
                    .withTransactionDate(LocalDateTime.now().toString())
                    .withTransactionType(TransactionType.DEBIT.getName())
                    .withAmount(100L)
                    .withCurrency("SEK").build();
            return Response.status(Status.OK).entity(response).build();
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
