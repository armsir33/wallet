package com.gears.leo;

import static com.gears.leo.infrastructure.ServiceHelper.addShutdownHook;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gears.leo.api.WalletRestService;
import com.gears.leo.domain.Service;
import com.gears.leo.domain.Shutdownable;
import com.gears.leo.domain.WalletHandleService;
import com.gears.leo.infrastructure.ServiceHelper;
import com.gears.leo.infrastructure.WalletConfiguration;
import com.gears.leo.infrastructure.WalletHandleServiceImpl;

public class WalletMain implements Shutdownable {

    private static final Logger LOG = LoggerFactory.getLogger(WalletMain.class);

    private final Service walletService;

    public WalletMain(final Service walletService) {
        this.walletService = Validate.notNull(walletService, "Valid walletService required");
    }

    public static WalletMain create() {
        final WalletConfiguration configuration = new WalletConfiguration();
        return new WalletMain(createWalletService(configuration, new ServiceHelper()));
    }

    public static void main(String[] args) {
        System.out.println("WalletMain.main()->");

        try {
            final WalletMain main = create();
            addShutdownHook(main);
        } catch (final Exception e) {
            System.err.println("WARNING Wallet failed to start. " + e.getMessage());
            LOG.error("Wallet failed to start. ", e);
            System.exit(-1);
        }
        LOG.info("****************************************");
        LOG.info("*** Wallet successfully started ***");
        LOG.info("****************************************");
    }

    private static Service createWalletService(WalletConfiguration configuration, ServiceHelper mainService) {

        final WalletHandleService walletHandleService = WalletHandleServiceImpl.create();
        final WalletRestService walletRestService = new WalletRestService(walletHandleService);

        return mainService.createRestService(configuration.getEndpointAddress(), walletRestService);
    }

    @Override
    public void shutdown() {
        LOG.info("-> WalletMain.shutdown()");
        this.walletService.stop();
        LOG.info("<- WalletMain.shutdown() STOPPED");
    }
}
