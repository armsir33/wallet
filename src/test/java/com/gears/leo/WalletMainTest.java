package com.gears.leo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WalletMainTest {

    private WalletMain main;

    @Before
    public void setUp() {
        main = WalletMain.create();
    }

    @After
    public void clearUp() {
        main.shutdown();
    }

    @Test
    public void testRunnable() {
        try (final Response response = WebClient.create("http://localhost:8080/status").accept(MediaType.TEXT_PLAIN_TYPE).get()) {
            assertThat(200, equalTo(response.getStatus()));
            assertThat("UP", equalTo(response.readEntity(String.class)));
        }
    }

}
