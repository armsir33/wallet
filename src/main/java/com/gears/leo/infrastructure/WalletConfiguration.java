package com.gears.leo.infrastructure;

public class WalletConfiguration {

    public static final int SERVICE_PORT = 8080;

    public String getEndpointAddress() {
        return "http://0.0.0.0:" + SERVICE_PORT;
    }

}
